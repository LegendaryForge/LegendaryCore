# LegendaryCore

LegendaryCore is a minimal, engine-agnostic foundation for deterministic legendary encounters in Hytale.

LegendaryCore aims to provide a small, deterministic encounter foundation with conservative, fail-closed behavior when required platform information is unavailable.

---

## Deterministic Lifecycle Semantics

LegendaryCore enforces strict lifecycle determinism:

- **Start** is emitted once when an encounter transitions from `CREATED → RUNNING`
- **End** is emitted once when an encounter transitions to `ENDED`
- **Leave** is idempotent and removes players from participants or spectators without mutating encounter state

These guarantees exist to prevent duplicate execution, partial teardown, and inconsistent encounter outcomes.

---

## Join and Spectator Behavior

- Capacity limits are enforced for both participants and spectators
- Spectator access is governed by `SpectatorPolicy`:
  - `DISALLOW` denies all spectator joins
  - View-only spectators are treated as passive observers and do **not** start encounters

---

## Access Policy Behavior

LegendaryCore defines conservative access semantics:

- `PUBLIC` participation is allowed by policy
- `PARTY_ONLY` participation is **fail-closed** when party context or party directory information is unavailable
- `INVITE_ONLY` is not implemented in the reference policy and denies by default

---

## Explicit Non-Goals (Unless Gameplay Requires)

LegendaryCore intentionally avoids:

- Additional time or clock refactors
- Matchmaking or invite systems without a clear gameplay requirement
- Persistence guarantees (reference implementation is in-memory only)

---

## Purpose

LegendaryCore is a shared foundation library for Legendary mods built for Hytale.

Its purpose is to provide stable, reusable primitives that multiple Legendary mods can depend on, without embedding gameplay logic or design assumptions into the core itself.

LegendaryCore exists to:

- Reduce duplication across Legendary mods
- Provide consistent patterns for common mod concerns
- Establish a stable internal ecosystem for Legendary content

It is **not** intended to be a gameplay mod on its own.

---

## What LegendaryCore Provides

LegendaryCore focuses on infrastructure, not content. Intended responsibilities include:

- Deterministic encounter lifecycle coordination
- Identity and namespacing primitives
- Deterministic registries and discovery mechanisms
- Access gating and policy contracts
- Event abstractions (mechanics only)
- Encounter models, states, and outcomes
- Stable service exposure patterns

All provided functionality is designed to be broadly reusable across multiple Legendary mods.

---

## What LegendaryCore Does Not Provide

LegendaryCore intentionally avoids:

- Gameplay logic or balance decisions
- Legendary item or encounter implementations
- Mod-specific configuration schemas
- Hard-coded content, tuning, or progression systems
- Assumptions about how individual Legendary mods should behave

Anything that expresses what a mod does belongs in that mod — not in LegendaryCore.

---

## API Stability

LegendaryCore distinguishes between:

- **Public API** (`core.api.*`) — intended for use by other mods and kept stable
- **Internal code** (`core.internal.*`) — subject to change without notice

Only the public API should be relied upon by other Legendary mods.

For detailed stability and versioning guarantees, see `API_STABILITY.md`.

---

## Core vs Platform Responsibility

LegendaryCore is intentionally platform-agnostic.

It defines shared mechanisms and contracts, not game runtime behavior.

### LegendaryCore Responsibilities

- Identity and namespacing primitives
- Deterministic registries and discovery
- Lifecycle coordination and service exposure
- Event abstractions (mechanics only)
- Encounter contracts and policies

### Platform Responsibilities (e.g. Hytale)

- Game event listeners and hooks
- Player, world, and entity interaction
- Threading, scheduling, and tick integration
- Runtime execution of encounters
- Translation between platform concepts and core APIs

Platform-specific code must live in adapter layers and must not leak into the public LegendaryCore API.

---

## Status

LegendaryCore is under active development.
Breaking changes may occur until an initial API stability baseline is declared.

---

## License

MIT License. See `LICENSE`.
