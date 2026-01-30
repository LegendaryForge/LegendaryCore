package io.github.legendaryforge.legendary.core.api.activation;

import io.github.legendaryforge.legendary.core.api.encounter.EncounterInstance;
import java.util.Optional;
import java.util.UUID;

public record ActivationAttemptResult(
        ActivationAttemptStatus status,
        ActivationDecision decision,
        Optional<UUID> sessionId,
        Optional<EncounterInstance> instance) {}
