package io.github.legendaryforge.legendary.core.internal.activation;

import io.github.legendaryforge.legendary.core.api.activation.ActivationAttemptResult;
import io.github.legendaryforge.legendary.core.api.activation.ActivationAttemptStatus;
import io.github.legendaryforge.legendary.core.api.activation.ActivationDecision;
import io.github.legendaryforge.legendary.core.api.activation.ActivationService;
import io.github.legendaryforge.legendary.core.api.activation.session.ActivationSessionBeginResult;
import io.github.legendaryforge.legendary.core.api.activation.session.ActivationSessionBeginStatus;
import io.github.legendaryforge.legendary.core.api.activation.session.ActivationSessionService;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterKey;
import io.github.legendaryforge.legendary.core.api.gate.ConditionGate;
import io.github.legendaryforge.legendary.core.api.gate.GateDecision;
import io.github.legendaryforge.legendary.core.api.gate.GateService;
import io.github.legendaryforge.legendary.core.api.id.ResourceId;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class DefaultActivationService implements ActivationService {

    private final GateService gates;
    private final ActivationSessionService sessions;

    public DefaultActivationService(GateService gates, ActivationSessionService sessions) {
        this.gates = Objects.requireNonNull(gates, "gates");
        this.sessions = Objects.requireNonNull(sessions, "sessions");
    }

    @Override
    public ActivationAttemptResult attemptActivation(ActivationAttemptRequest request) {
        Objects.requireNonNull(request, "request");

        EncounterKey encounterKey = EncounterKey.of(request.definition(), request.context());

        // Phase 2: evaluate activation gate (if present)
        ActivationDecision decision = request.activationGateKey()
                .map(gateKey -> evaluateGate(gateKey, request, encounterKey))
                .orElseGet(ActivationDecision::allow);

        if (!decision.allowed()) {
            return new ActivationAttemptResult(
                    ActivationAttemptStatus.FAILED,
                    decision,
                    Optional.empty(),
                    Optional.empty());
        }

        // Phase 2: begin activation session
        ActivationSessionBeginResult begin = sessions.begin(
                new ActivationSessionService.ActivationSessionBeginRequest(
                        request.activatorId(),
                        encounterKey,
                        request.definition(),
                        request.context(),
                        request.activationGateKey(),
                        decision.attributes()
                )
        );

        if (begin.status() == ActivationSessionBeginStatus.CREATED || begin.status() == ActivationSessionBeginStatus.EXISTING) {
            return new ActivationAttemptResult(
                    ActivationAttemptStatus.SUCCESS,
                    decision,
                    Optional.of(begin.sessionId()),
                    Optional.empty());
        }

        // DENIED => FAILED
        ActivationDecision denied = ActivationDecision.deny(begin.reasonCode(), decision.attributes());
        return new ActivationAttemptResult(
                ActivationAttemptStatus.FAILED,
                denied,
                Optional.empty(),
                Optional.empty());
    }

    private ActivationDecision evaluateGate(ResourceId gateKey, ActivationAttemptRequest request, EncounterKey encounterKey) {
        ConditionGate.GateRequest gateRequest = new ConditionGate.GateRequest(
                gateKey,
                request.activatorId(),
                Optional.of(encounterKey),
                Optional.empty(),
                Optional.empty(),
                Map.of()
        );

        GateDecision gateDecision = gates.evaluate(gateRequest);
        return gateDecision.allowed()
                ? ActivationDecision.allow()
                : ActivationDecision.deny(gateDecision.reasonCode(), gateDecision.attributes());
    }
}
