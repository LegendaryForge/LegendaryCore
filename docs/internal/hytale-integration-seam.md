# Hytale Integration Seam (Internal)

LegendaryCore intentionally contains no Hytale imports and no gameplay enforcement.

## Purpose
- Platform-integrated code (with real Hytale imports) binds to LegendaryCore via internal seams and posts internal events.

## Seam
- `io.github.legendaryforge.legendary.core.internal.platform.hytale.HytaleSignalSource`
- Bound via `new HytalePlatformAdapter(runtime, signalSource)`

## Arena bounds enforcement pipeline (current)
1) Platform detects breach (player outside bounds for an instance).
2) Platform posts `ArenaBoundsViolatedEvent` onto `runtime.events()`.
3) Core `BoundsInvariant` emits `ArenaParticipationRevokedEvent` (signal-only).
4) Platform enforces behavior (spectate/lockout/etc).

## Non-goals
- No tick/movement hooks in LegendaryCore.
- No teleporting/damage/physics correction in LegendaryCore.
- No Hytale imports in LegendaryCore.

