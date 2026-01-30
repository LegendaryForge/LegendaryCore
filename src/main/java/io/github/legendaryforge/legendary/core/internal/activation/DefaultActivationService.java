package io.github.legendaryforge.legendary.core.internal.activation;

import io.github.legendaryforge.legendary.core.api.activation.ActivationAttemptResult;
import io.github.legendaryforge.legendary.core.api.activation.ActivationAttemptStatus;
import io.github.legendaryforge.legendary.core.api.activation.ActivationDecision;
import io.github.legendaryforge.legendary.core.api.activation.ActivationService;
import io.github.legendaryforge.legendary.core.api.id.ResourceId;
import java.util.Optional;

public final class DefaultActivationService implements ActivationService {

    @Override
    public ActivationAttemptResult attemptActivation(ActivationAttemptRequest request) {
        // Intentionally inert until Phase 1 · Step 3 wiring (authority + gate + session + encounter creation).
        return new ActivationAttemptResult(
                ActivationAttemptStatus.FAILED,
                ActivationDecision.deny(ResourceId.of("legendarycore", "not_wired")),
                Optional.empty(),
                Optional.empty());
    }
}
