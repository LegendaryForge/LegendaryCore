# Changelog

## Unreleased

## M1: Encounter correctness groundwork (pre-1.0)
This milestone locks down core encounter lifecycle behavior with deterministic, fail-closed policies and test coverage.

### Encounter join/spectate correctness
- Join policy enforcement and spectator policy enforcement are applied consistently at the manager level.
- Capacity is enforced for both participants and spectators:
  - Participants: deny when at max capacity (unless already present)
  - Spectators: deny when at max capacity (unless already present)

### Lifecycle determinism
- Encounter start event is emitted exactly once (including under concurrent participant joins).
- Encounter end event is emitted exactly once (including under concurrent end calls).
- Leave semantics are stable and idempotent:
  - Leaving removes the player from participants/spectators
  - Leaving does not change encounter state
  - Leaving does not emit additional lifecycle events
- Reuse semantics are stable:
  - create() may reuse an existing non-ended instance by deterministic key when reuse policy allows
  - ended instances are not reused

### Fail-closed access behavior
- PARTY_ONLY participation fails closed when party context or PartyDirectory is unavailable (integration tested).
- SpectatorPolicy.DISALLOW denies spectator joins (integration tested).

### Telemetry and runtime seams
- Telemetry duration uses an injected Clock (testable), with a default runtime Clock provided by CoreRuntime.

