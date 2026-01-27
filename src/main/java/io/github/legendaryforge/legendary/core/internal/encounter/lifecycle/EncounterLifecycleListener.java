package io.github.legendaryforge.legendary.core.internal.encounter.lifecycle;

import io.github.legendaryforge.legendary.core.api.encounter.event.EncounterCreatedEvent;
import io.github.legendaryforge.legendary.core.api.encounter.event.EncounterEndedEvent;
import io.github.legendaryforge.legendary.core.api.encounter.event.EncounterReusedEvent;

@FunctionalInterface
public interface EncounterLifecycleListener {

    void onCreated(EncounterCreatedEvent event);

    default void onReused(EncounterReusedEvent event) {}

    default void onEnded(EncounterEndedEvent event) {}
}
