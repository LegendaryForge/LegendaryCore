package io.github.legendaryforge.legendary.core.api.platform;

import io.github.legendaryforge.legendary.core.api.encounter.EncounterManager;
import io.github.legendaryforge.legendary.core.api.event.EventBus;
import io.github.legendaryforge.legendary.core.api.lifecycle.Lifecycle;
import io.github.legendaryforge.legendary.core.api.lifecycle.ServiceRegistry;
import io.github.legendaryforge.legendary.core.api.registry.RegistryAccess;

/**
 * Platform-provided runtime access point for LegendaryCore.
 *
 * <p>Platforms (e.g. Hytale) are responsible for creating and wiring an implementation of this
 * interface. Legendary mods should depend only on this interface and other public {@code core.api.*}
 * types, not on platform-specific classes.</p>
 */
public interface CoreRuntime {

    RegistryAccess registries();

    Lifecycle lifecycle();

    ServiceRegistry services();

    EventBus events();

    EncounterManager encounters();
}
