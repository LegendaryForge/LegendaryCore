package io.github.legendaryforge.legendary.core.internal.encounter;

import io.github.legendaryforge.legendary.core.api.encounter.EncounterContext;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterDefinition;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterInstance;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterManager;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterState;
import io.github.legendaryforge.legendary.core.api.encounter.EndReason;
import io.github.legendaryforge.legendary.core.api.encounter.JoinResult;
import io.github.legendaryforge.legendary.core.api.encounter.ParticipationRole;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Minimal, platform-agnostic internal reference implementation of {@link EncounterManager}.
 *
 * <p>This implementation is intentionally policy-light and in-memory only. It exists to allow
 * {@code DefaultCoreRuntime} to be constructed without any platform adapter present.</p>
 *
 * <p>Do not add platform concepts here (worlds, players, tick loops, etc.). Platform adapters should
 * adapt their environment to this core API, not the reverse.</p>
 */
public final class DefaultEncounterManager implements EncounterManager {

    private final Map<UUID, DefaultEncounterInstance> instances = new ConcurrentHashMap<>();

    @Override
    public EncounterInstance create(EncounterDefinition definition, EncounterContext context) {
        java.util.Objects.requireNonNull(definition, "definition");
        java.util.Objects.requireNonNull(context, "context");

        UUID id = UUID.randomUUID();
        DefaultEncounterInstance instance =
                new DefaultEncounterInstance(id, definition, context);

        instances.put(id, instance);
        return instance;
    }

    @Override
    public JoinResult join(UUID playerId, EncounterInstance instance, ParticipationRole role) {
        java.util.Objects.requireNonNull(playerId, "playerId");
        java.util.Objects.requireNonNull(instance, "instance");
        java.util.Objects.requireNonNull(role, "role");

        if (!(instance instanceof DefaultEncounterInstance)) {
            return JoinResult.DENIED_POLICY;
        }

        DefaultEncounterInstance i = (DefaultEncounterInstance) instance;

        // Must be owned by this manager.
        if (instances.get(i.instanceId) != i) {
            return JoinResult.DENIED_POLICY;
        }

        if (i.state == EncounterState.ENDED) {
            return JoinResult.DENIED_STATE;
        }

        if (i.participants.contains(playerId) || i.spectators.contains(playerId)) {
            // Idempotent success: already in the requested or other role.
            return JoinResult.SUCCESS;
        }

        // Policy checks (platform-agnostic reference behavior):
        // - Participants: only PUBLIC is joinable without platform semantics.
        // - Spectators: only allowed when SpectatorPolicy permits.
        if (role == ParticipationRole.PARTICIPANT) {
            if (i.definition.accessPolicy() != io.github.legendaryforge.legendary.core.api.encounter.EncounterAccessPolicy.PUBLIC) {
                return JoinResult.DENIED_POLICY;
            }

            int max = i.definition.maxParticipants();
            if (max > 0 && i.participants.size() >= max) {
                return JoinResult.DENIED_FULL;
            }

            i.participants.add(playerId);
            if (i.state == EncounterState.CREATED) {
                i.state = EncounterState.RUNNING;
            }
            return JoinResult.SUCCESS;
        }

        // SPECTATOR
        if (i.definition.spectatorPolicy() != io.github.legendaryforge.legendary.core.api.encounter.SpectatorPolicy.ALLOW_VIEW_ONLY) {
            return JoinResult.DENIED_POLICY;
        }

        int max = i.definition.maxSpectators();
        if (max > 0 && i.spectators.size() >= max) {
            return JoinResult.DENIED_FULL;
        }

        i.spectators.add(playerId);
        return JoinResult.SUCCESS;
    }

    @Override
    public void leave(UUID playerId, EncounterInstance instance) {
        java.util.Objects.requireNonNull(playerId, "playerId");
        java.util.Objects.requireNonNull(instance, "instance");

        if (!(instance instanceof DefaultEncounterInstance)) {
            return;
        }

        DefaultEncounterInstance i = (DefaultEncounterInstance) instance;

        // Must be owned by this manager.
        if (instances.get(i.instanceId) != i) {
            return;
        }

        if (i.state == EncounterState.ENDED) {
            return;
        }

        synchronized (i) {
            i.participants.remove(playerId);
            i.spectators.remove(playerId);
        }
    }

    @Override
    public void end(EncounterInstance instance, EndReason reason) {
        java.util.Objects.requireNonNull(instance, "instance");
        java.util.Objects.requireNonNull(reason, "reason");

        if (!(instance instanceof DefaultEncounterInstance)) {
            return;
        }

        DefaultEncounterInstance i = (DefaultEncounterInstance) instance;

        // Must be owned by this manager.
        if (instances.get(i.instanceId) != i) {
            return;
        }

        synchronized (i) {
            i.state = EncounterState.ENDED;
        }

        instances.remove(i.instanceId, i);
    }

    @Override
    public Optional<EncounterInstance> byInstanceId(UUID instanceId) {
        return Optional.ofNullable(instances.get(instanceId));
    }

    private static final class DefaultEncounterInstance implements EncounterInstance {

        private final UUID instanceId;
        private final EncounterDefinition definition;
        private final EncounterContext context;

        private EncounterState state;

        private final Set<UUID> participants = new LinkedHashSet<>();
        private final Set<UUID> spectators = new LinkedHashSet<>();

        private DefaultEncounterInstance(UUID instanceId,
                                         EncounterDefinition definition,
                                         EncounterContext context) {
            this.instanceId = instanceId;
            this.definition = definition;
            this.context = context;
            this.state = EncounterState.CREATED;
        }

        @Override
        public UUID instanceId() {
            return instanceId;
        }

        @Override
        public EncounterDefinition definition() {
            return definition;
        }

        @Override
        public EncounterState state() {
            return state;
        }

        @Override
        public Set<UUID> participants() {
            return Collections.unmodifiableSet(participants);
        }

        @Override
        public Set<UUID> spectators() {
            return Collections.unmodifiableSet(spectators);
        }
    }
}
