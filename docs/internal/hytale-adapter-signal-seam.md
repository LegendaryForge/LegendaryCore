# Hytale Adapter Signal Seam (Internal)

This document describes how platform-integrated modules should emit internal arena signals without adding Hytale imports or gameplay logic to LegendaryCore.

## Why this exists
- LegendaryCore must remain platform-agnostic and signal-only.
- Platform modules (with real Hytale imports) detect gameplay conditions and post internal events onto the CoreRuntime EventBus.

## Seam
- `io.github.legendaryforge.legendary.core.internal.platform.hytale.HytaleSignalSource`
- Bound via `new HytalePlatformAdapter(runtime, signalSource)`

## Arena bounds enforcement pipeline (current)
1. Platform module detects a breach (player outside arena bounds for an encounter instance).
2. Platform module posts:
   - `ArenaBoundsViolatedEvent.withoutPosition(instanceId, playerId)`
3. Core `BoundsInvariant` (legendary-only, instance-gated) posts:
   - `ArenaParticipationRevokedEvent(instanceId, playerId)`
4. Platform module reacts (behavior lives here):
   - move player to spectator / prevent actions / show UI warning / etc.

## Non-goals (explicit)
- No teleporting or physics correction inside LegendaryCore.
- No Hytale imports in LegendaryCore.
- No public API changes for bounds enforcement.

## Minimum implementation notes for platform module
- You will need a way to map `playerId -> encounter instanceId` (content/platform-owned).
- You will need arena bounds data (content/platform-owned): center + radius (v1), or AABB later.
- Emit violations on tick or movement updates; de-dupe or rate-limit in platform module if desired (Core already de-dupes revoke per player per instance).

