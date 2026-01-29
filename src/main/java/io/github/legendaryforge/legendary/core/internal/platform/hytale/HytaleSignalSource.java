package io.github.legendaryforge.legendary.core.internal.platform.hytale;

import io.github.legendaryforge.legendary.core.api.platform.CoreRuntime;
import java.util.Objects;

/**
 * Internal integration seam for Hytale platform signals.
 *
 * <p>Implementations may be provided by a platform-integrated module (with real Hytale imports) and
 * post internal events onto the runtime EventBus. LegendaryCore itself stays platform-agnostic.
 */
@FunctionalInterface
public interface HytaleSignalSource {

    void bind(CoreRuntime runtime);

    static HytaleSignalSource noop() {
        return runtime -> Objects.requireNonNull(runtime, "runtime");
    }
}
