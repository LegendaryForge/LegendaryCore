package io.github.legendaryforge.legendary.core.internal.runtime;

import io.github.legendaryforge.legendary.core.api.platform.CoreRuntime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

final class DefaultCoreRuntimeWiringTest {

    @Test
    void defaultConstructorWiresAllCoreComponents() {
        CoreRuntime runtime = new DefaultCoreRuntime();

        assertNotNull(runtime.registries(), "registries");
        assertNotNull(runtime.lifecycle(), "lifecycle");
        assertNotNull(runtime.services(), "services");
        assertNotNull(runtime.events(), "events");
        assertNotNull(runtime.encounters(), "encounters");
    }
}
