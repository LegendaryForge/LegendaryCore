package io.github.legendaryforge.legendary.core.internal.legendary.arena;

import io.github.legendaryforge.legendary.core.api.encounter.EncounterContext;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterDefinition;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterInstance;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterKey;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterManager;
import io.github.legendaryforge.legendary.core.api.encounter.EndReason;
import io.github.legendaryforge.legendary.core.api.encounter.JoinResult;
import io.github.legendaryforge.legendary.core.api.encounter.ParticipationRole;
import io.github.legendaryforge.legendary.core.api.id.ResourceId;
import io.github.legendaryforge.legendary.core.api.legendary.definition.LegendaryEncounterDefinition;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Internal decorator that tracks which encounter definition ids are legendary based on runtime create() calls.
 *
 * <p>This provides a deterministic, low-overhead gating mechanism for internal arena invariants without introducing
 * new public Core APIs or registries.
 */
public final class LegendaryDefinitionTrackingEncounterManager implements EncounterManager {

    private final EncounterManager delegate;
    private final Set<ResourceId> legendaryDefinitionIds;

    public LegendaryDefinitionTrackingEncounterManager(EncounterManager delegate, Set<ResourceId> legendaryDefinitionIds) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
        this.legendaryDefinitionIds = Objects.requireNonNull(legendaryDefinitionIds, "legendaryDefinitionIds");
    }

    public boolean isLegendary(ResourceId definitionId) {
        Objects.requireNonNull(definitionId, "definitionId");
        return legendaryDefinitionIds.contains(definitionId);
    }

    @Override
    public EncounterInstance create(EncounterDefinition definition, EncounterContext context) {
        Objects.requireNonNull(definition, "definition");
        if (definition instanceof LegendaryEncounterDefinition) {
            legendaryDefinitionIds.add(definition.id());
        }
        return delegate.create(definition, context);
    }

    @Override
    public JoinResult join(UUID playerId, EncounterInstance instance, ParticipationRole role) {
        return delegate.join(playerId, instance, role);
    }

    @Override
    public void leave(UUID playerId, EncounterInstance instance) {
        delegate.leave(playerId, instance);
    }

    @Override
    public void end(EncounterInstance instance, EndReason reason) {
        delegate.end(instance, reason);
    }

    @Override
    public Optional<EncounterInstance> byInstanceId(UUID instanceId) {
        return delegate.byInstanceId(instanceId);
    }

    @Override
    public Optional<EncounterInstance> byKey(EncounterKey key) {
        return delegate.byKey(key);
    }
}
