# Hytale Integration Seam (Internal)

This document describes how LegendaryCore is intended to integrate with the Hytale runtime.
It exists to prevent architectural drift and accidental platform coupling.

---

## Core principles
- LegendaryCore defines **mechanisms**, never gameplay policy.
- LegendaryCore contains **no Hytale imports**.
- All enforcement (movement, teleport, damage, spectate) is platform-owned.

---

## Integration seam
Platform code binds to LegendaryCore via:

- `io.github.legendaryforge.legendary.core.internal.platform.hytale.HytaleSignalSource`
- `HytalePlatformAdapter(CoreRuntime, HytaleSignalSource)`

This seam allows platform code to:
- Observe engine state (ticks, movement, entities)
- Translate engine events into LegendaryCore signals

---

## Arena bounds enforcement pipeline (current)

1. Platform detects a bounds breach (player outside arena region).
2. Platform posts `ArenaBoundsViolatedEvent` to `CoreRuntime.events()`.
3. Core `BoundsInvariant` evaluates the signal.
4. Core emits `ArenaParticipationRevokedEvent` (signal only).
5. Platform enforces behavior (spectator mode, lockout, etc).

LegendaryCore never:
- Reads player position
- Tracks ticks
- Applies gameplay consequences

---

## Non-goals (explicit)
- No tick loops in LegendaryCore.
- No geometry math in LegendaryCore.
- No world, block, or entity queries.
- No enforcement logic.

---

## When to implement platform enforcement
Platform-specific enforcement should only be implemented once:
- Hytale exposes stable movement or tick hooks
- Player identity mapping is well-defined
- Arena geometry representation is finalized

Until then, the seam remains intentionally minimal.

