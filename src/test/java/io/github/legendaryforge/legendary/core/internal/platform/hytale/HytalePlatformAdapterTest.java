package io.github.legendaryforge.legendary.core.internal.platform.hytale;

import static org.junit.jupiter.api.Assertions.*;

import io.github.legendaryforge.legendary.core.api.encounter.EncounterManager;
import io.github.legendaryforge.legendary.core.api.event.EventBus;
import io.github.legendaryforge.legendary.core.api.lifecycle.Lifecycle;
import io.github.legendaryforge.legendary.core.api.lifecycle.ServiceRegistry;
import io.github.legendaryforge.legendary.core.api.platform.CoreRuntime;
import io.github.legendaryforge.legendary.core.api.registry.RegistryAccess;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

final class HytalePlatformAdapterTest {

    @Test
    void bindsSignalSourceToRuntime() {
        CoreRuntime runtime = new CoreRuntime() {
            @Override
            public RegistryAccess registries() {
                return null;
            }

            @Override
            public Lifecycle lifecycle() {
                return null;
            }

            @Override
            public ServiceRegistry services() {
                return null;
            }

            @Override
            public EventBus events() {
                return null;
            }

            @Override
            public EncounterManager encounters() {
                return null;
            }
        };

        AtomicReference<CoreRuntime> seen = new AtomicReference<>();
        HytaleSignalSource source = seen::set;

        new HytalePlatformAdapter(runtime, source);
        assertSame(runtime, seen.get());
    }
}
