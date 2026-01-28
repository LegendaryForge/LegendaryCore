package io.github.legendaryforge.legendary.core.internal.legendary.arena;

import io.github.legendaryforge.legendary.core.api.encounter.event.EncounterCleanupEvent;
import io.github.legendaryforge.legendary.core.api.encounter.event.EncounterEndedEvent;
import io.github.legendaryforge.legendary.core.api.encounter.event.EncounterStartedEvent;
import io.github.legendaryforge.legendary.core.api.event.EventBus;
import io.github.legendaryforge.legendary.core.api.event.Subscription;
import io.github.legendaryforge.legendary.core.api.id.ResourceId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Internal bridge that forwards encounter lifecycle signals to arena invariants.
 *
 * <p>Key behavior: cleanup routing is deterministic without expanding public events.
 * Cleanup events only include instanceId, so we remember instanceId -> definitionId from start.
 */
public final class ArenaInvariantBridge {

    private final ArenaInvariantRegistry registry;

    // Cleanup event only carries instanceId; this mapping routes cleanup to invariants.
    private final ConcurrentHashMap<UUID, ResourceId> instanceToDefinition = new ConcurrentHashMap<>();

    public ArenaInvariantBridge(ArenaInvariantRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry");
    }

    public void onStarted(EncounterStartedEvent event) {
        Objects.requireNonNull(event, "event");
        instanceToDefinition.put(event.instanceId(), event.definitionId());
        for (ArenaInvariant inv : registry.invariantsFor(event.definitionId())) {
            inv.onStart(event.instanceId());
        }
    }

    public void onEnded(EncounterEndedEvent event) {
        Objects.requireNonNull(event, "event");
        for (ArenaInvariant inv : registry.invariantsFor(event.definitionId())) {
            inv.onEnd(event.instanceId());
        }
    }

    public void onCleanup(EncounterCleanupEvent event) {
        Objects.requireNonNull(event, "event");
        ResourceId defId = instanceToDefinition.remove(event.instanceId());
        if (defId == null) {
            return;
        }
        for (ArenaInvariant inv : registry.invariantsFor(defId)) {
            inv.onCleanup(event.instanceId());
        }
    }

    public static List<Subscription> bind(EventBus bus, ArenaInvariantRegistry registry) {
        Objects.requireNonNull(bus, "bus");
        Objects.requireNonNull(registry, "registry");

        ArenaInvariantBridge bridge = new ArenaInvariantBridge(registry);
        List<Subscription> subs = new ArrayList<>(3);

        subs.add(bus.subscribe(EncounterStartedEvent.class, bridge::onStarted));
        subs.add(bus.subscribe(EncounterEndedEvent.class, bridge::onEnded));
        subs.add(bus.subscribe(EncounterCleanupEvent.class, bridge::onCleanup));

        return subs;
    }
}
