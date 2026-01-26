# LegendaryCore – API Stability & Versioning Policy

This document defines stability guarantees, versioning rules, and architectural boundaries
for LegendaryCore.

Its purpose is to protect the long-term health of the Legendary mod ecosystem by making
expectations explicit and enforceable.

---

## Package Classification

LegendaryCore code is divided into the following categories.

### 1. Public Stable API (`core.api.*`)

These packages define the supported extension surface for Legendary mods.

Characteristics:
- Intended for direct use by external mods
- Backward compatibility is a priority
- Breaking changes are rare and deliberate

Examples include:
- Identity primitives (e.g. ResourceId)
- Registries and registration APIs
- Lifecycle and service coordination
- Encounter model interfaces

Once stabilized, APIs in this category follow strict semantic versioning rules.

---

### 2. Public Extensible API (`core.api.*`, evolving)

Some public APIs are intentionally minimal and expected to grow over time.

Characteristics:
- Additive evolution is expected
- Existing behavior should not be broken without a major version bump
- Conceptual models should remain stable even as features expand

Examples include:
- Event bus abstractions
- Encounter system interfaces and policies

---

### 3. Internal Implementation (`core.internal.*`)

Internal packages contain implementation details.

Characteristics:
- No compatibility guarantees
- May be refactored, replaced, or removed at any time
- Must not be relied upon by external mods

All production behavior must be reachable through the public API.

---

### 4. Platform-Specific Integration (future)

Platform bindings (e.g. Hytale server integration) belong in platform-specific internal
packages.

Expected location:
- `core.internal.platform.<platform>`

Rules:
- Public APIs must not reference platform-specific classes
- Platform code must adapt the platform to the core, not the reverse
- CI must not require a platform runtime to be present

---

## Versioning Policy

LegendaryCore uses semantic versioning: `MAJOR.MINOR.PATCH`.

### PATCH
- Bug fixes
- Internal refactors
- Test changes
- No public API changes

### MINOR
- Additive changes to public APIs
- New interfaces, methods, or enum values
- No required changes for existing consumers

### MAJOR
- Breaking public API changes
- Behavioral changes that invalidate existing assumptions
- Removal or redesign of core concepts

Breaking changes require:
- Clear documentation
- Migration guidance
- Explicit justification

---

## Breaking Change Definition

A breaking change includes, but is not limited to:
- Removing or renaming public types, methods, or fields
- Changing method signatures or return contracts
- Altering lifecycle ordering or guarantees
- Changing registry or service semantics
- Modifying encounter or event behavior in incompatible ways

---

## Supported Extension Points (v0.1)

LegendaryCore currently supports extension by external mods through the following public
API surfaces.

1. Identity
   - `ResourceId` (`core.api.id`)
   - Used for stable namespacing and deterministic identification of all mod-declared values.

2. Registries
   - `RegistryKey`, `Registry`, `Registrar`, `RegistryAccess` (`core.api.registry`)
   - Used for declaring and registering mod-owned content and systems in a deterministic,
     validated way.

3. Lifecycle + Services
   - `Lifecycle`, `LifecyclePhase`, `ServiceRegistry` (`core.api.lifecycle`)
   - Used to coordinate initialization ordering and to expose services safely, with lifecycle
     enforcement to prevent late mutation.

4. Events
   - `EventBus`, `Event`, `EventListener`, `Subscription` (`core.api.event`)
   - Used for event publication and subscription. Current behavior is exact-class dispatch.

5. Encounters
   - `EncounterDefinition`, `EncounterContext`, `EncounterInstance`, `EncounterManager`
     and associated policy enums (`core.api.encounter`)
   - Used to model private/party-first legendary encounters. Spectating is supported as a
     view-only role for late joiners.

---

## Design Principles

- Core defines *mechanisms*, not *policy*
- Platforms adapt to the core, never the opposite
- Extension points are explicit and intentional
- Defaults are conservative and deterministic
- Internal code exists to serve the public API, not bypass it

---

## Status

Until a 1.0.0 release, breaking changes may occur, but they must still follow the spirit
of this document.

Once 1.0.0 is released, the rules above apply strictly.
