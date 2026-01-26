package io.github.legendaryforge.legendary.core.internal.platform.hytale;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.legendaryforge.legendary.core.api.platform.CoreRuntime;
import org.junit.jupiter.api.Test;

final class HytalePlatformAdapterTest {

    @Test
    void defaultConstructorCreatesRuntime() {
        HytalePlatformAdapter adapter = new HytalePlatformAdapter();
        CoreRuntime runtime = adapter.runtime();

        assertNotNull(runtime, "runtime");
        assertNotNull(runtime.encounters(), "runtime.encounters");
    }
}
