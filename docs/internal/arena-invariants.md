# Arena Invariants (Internal Contract)

Status: INTERNAL-ONLY (LegendaryCore)

## Purpose
Arena invariants are internal enforcement rules used to keep encounter/arena lifecycle behavior deterministic.
They must not introduce new public Core APIs and must not leak behavior into non-Legendary encounters.

## Where they run (event points)
- Invariants are evaluated via internal bridges in response to encounter lifecycle events.
- Phase enforcement is driven by:
  - EncounterStartedEvent
  - EncounterEndedEvent
  - EncounterCleanupEvent

## Allowed state
- Invariants may hold per-encounter-instance runtime state only.
- State must be keyed by instance identity and must be cleaned up on end/cleanup paths.
- State must not require global registries or persistence.

## Forbidden behavior (hard rules)
- No public API additions or changes (Core remains signal-only).
- No world/terrain mutation or permanent shared-state mutation.
- No player inventory/reward mutation and no reward policy decisions.
- No “side effects” beyond internal enforcement (e.g., rejecting invalid transitions).

## Determinism requirements
- Given the same event sequence, invariants must produce the same outcomes.
- Invariants must not depend on wall-clock time unless a gameplay requirement explicitly demands it.

## Legendary-only gating (LOCKED)
- Arena invariants apply only to Legendary encounters.
- Legendary detection is captured at encounter creation time by tracking definition IDs when:
  - definition instanceof LegendaryEncounterDefinition
- Gating is enforced internally in DefaultCoreRuntime wiring.

## Current implementations
- PhaseGateInvariant
  - Tracks per-instance phase: ACTIVE or ENDED
  - Enforces no illegal phase transitions after end

## Scope guardrail
Do not generalize or promote invariants/legendary traits into public abstractions until real content pressure proves the need.
