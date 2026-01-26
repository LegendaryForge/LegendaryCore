# LegendaryCore

LegendaryCore is a shared foundation library for Legendary mods built for Hytale.

Its purpose is to provide stable, reusable primitives that multiple Legendary mods can depend on,
without embedding gameplay logic or design assumptions into the core itself.

---

## Purpose

LegendaryCore exists to:

- Reduce duplication across Legendary mods
- Provide consistent patterns for common mod concerns
- Establish a stable internal ecosystem for Legendary content

It is not intended to be a gameplay mod on its own.

---

## What LegendaryCore Provides

LegendaryCore focuses on **infrastructure**, not content. Intended responsibilities include:

- Plugin lifecycle utilities and common bootstrap patterns
- Shared logging and diagnostics helpers
- Configuration loading and validation primitives
- Serialization and data-format helpers
- Registration and discovery patterns for mod-declared systems
- Common constants, naming conventions, and utilities

All provided functionality is designed to be broadly reusable across multiple mods.

---

## What LegendaryCore Does *Not* Provide

LegendaryCore intentionally avoids:

- Gameplay logic or balance decisions
- Legendary item or encounter implementations
- Mod-specific configuration schemas
- Hard-coded content, tuning, or progression systems
- Assumptions about how individual Legendary mods should behave

Anything that expresses *what a mod does* belongs in that mod — not in LegendaryCore.

---

## API Stability

LegendaryCore distinguishes between:

- **Public API** (`core.api.*`): intended for use by other mods and kept stable
- **Internal code** (`core.internal.*`): subject to change without notice

Only the public API should be relied upon by other Legendary mods.

---

## Core vs Platform Responsibility

LegendaryCore is intentionally platform-agnostic.

It defines shared **mechanisms and contracts**, not game runtime behavior.

### LegendaryCore Responsibilities
- Identity and namespacing primitives
- Deterministic registries and discovery
- Lifecycle coordination and service exposure
- Event abstractions (mechanics only)
- Encounter *models* and policies (interfaces, states, outcomes)

### Platform Responsibilities (e.g. Hytale)
- Game event listeners and hooks
- Player, world, and entity interaction
- Threading, scheduling, and tick integration
- Runtime execution of encounters
- Translation between platform concepts and core APIs

Platform-specific code is expected to live in internal adapter layers and must not
leak into the public LegendaryCore API.

For detailed stability and versioning guarantees, see `API_STABILITY.md`.

---

## Status

LegendaryCore is under active development and its public API surface is still being defined.

Breaking changes may occur until an initial stability baseline is declared.

---

## License

(TBD)
