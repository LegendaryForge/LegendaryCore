package io.github.legendaryforge.legendary.core.internal.encounter.lifecycle;

import io.github.legendaryforge.legendary.core.api.encounter.event.EncounterCreatedEvent;
import io.github.legendaryforge.legendary.core.api.encounter.event.EncounterEndedEvent;
import io.github.legendaryforge.legendary.core.api.encounter.event.EncounterReusedEvent;
import io.github.legendaryforge.legendary.core.api.encounter.event.EncounterStartedEvent;
import io.github.legendaryforge.legendary.core.api.event.EventBus;
import io.github.legendaryforge.legendary.core.api.event.Subscription;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface EncounterTelemetryListener {
    void onCreated(EncounterCreatedEvent event);

    void onReused(EncounterReusedEvent event);

    void onStarted(EncounterStartedEvent event);

    void onEnded(EncounterEndedEvent event);

    static List<Subscription> bind(Optional<EventBus> events, EncounterTelemetryListener... listeners) {
        Objects.requireNonNull(events, "events");
        Objects.requireNonNull(listeners, "listeners");
        List<Subscription> subs = new ArrayList<>();
        events.ifPresent(bus -> {
            for (EncounterTelemetryListener l : listeners) {
                subs.add(bus.subscribe(EncounterCreatedEvent.class, l::onCreated));
                subs.add(bus.subscribe(EncounterReusedEvent.class, l::onReused));
                subs.add(bus.subscribe(EncounterStartedEvent.class, l::onStarted));
                subs.add(bus.subscribe(EncounterEndedEvent.class, l::onEnded));
            }
        });
        return subs;
    }
}
