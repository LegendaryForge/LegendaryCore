package io.github.legendaryforge.legendary.core.internal.runtime;

import io.github.legendaryforge.legendary.core.api.encounter.EncounterManager;
import io.github.legendaryforge.legendary.core.api.event.EventBus;
import io.github.legendaryforge.legendary.core.api.lifecycle.Lifecycle;
import io.github.legendaryforge.legendary.core.api.lifecycle.ServiceRegistry;
import io.github.legendaryforge.legendary.core.api.platform.CoreRuntime;
import io.github.legendaryforge.legendary.core.api.registry.RegistryAccess;
import io.github.legendaryforge.legendary.core.internal.encounter.DefaultEncounterManager;
import io.github.legendaryforge.legendary.core.internal.event.SimpleEventBus;
import io.github.legendaryforge.legendary.core.internal.lifecycle.DefaultLifecycle;
import io.github.legendaryforge.legendary.core.internal.lifecycle.DefaultServiceRegistry;
import io.github.legendaryforge.legendary.core.internal.registry.DefaultRegistryAccess;

import java.util.Objects;

/**
 * Default internal wiring of LegendaryCore runtime components.
 *
 * <p>This provides a platform-agnostic reference implementation that platform adapters may
 * construct and delegate to.</p>
 */
public final class DefaultCoreRuntime implements CoreRuntime {

    private final RegistryAccess registries;
    private final Lifecycle lifecycle;
    private final ServiceRegistry services;
    private final EventBus events;
    private final EncounterManager encounters;

    /**
     * Platform-agnostic default constructor using the internal reference EncounterManager.
     */
    public DefaultCoreRuntime() {
        this(new DefaultEncounterManager());
    }

    public DefaultCoreRuntime(EncounterManager encounters) {
        this.registries = new DefaultRegistryAccess();

        DefaultLifecycle lifecycle = new DefaultLifecycle();
        this.lifecycle = lifecycle;

        this.services = new DefaultServiceRegistry(lifecycle);
        this.events = new SimpleEventBus();
        this.encounters = Objects.requireNonNull(encounters, "encounters");
    }

    @Override
    public RegistryAccess registries() {
        return registries;
    }

    @Override
    public Lifecycle lifecycle() {
        return lifecycle;
    }

    @Override
    public ServiceRegistry services() {
        return services;
    }

    @Override
    public EventBus events() {
        return events;
    }

    @Override
    public EncounterManager encounters() {
        return encounters;
    }
}
