package io.github.legendaryforge.legendary.core.api.encounter;

import io.github.legendaryforge.legendary.core.api.id.ResourceId;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface EncounterContext {

    /**
     * The party that owns the encounter, if applicable.
     */
    Optional<UUID> partyId();

    /**
     * World identifier (implementation-defined).
     */
    ResourceId worldId();

    /**
     * Implementation-defined metadata (kept generic in v0.1).
     */
    Map<String, Object> metadata();
}
