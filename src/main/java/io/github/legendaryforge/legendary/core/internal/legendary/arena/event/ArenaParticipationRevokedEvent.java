package io.github.legendaryforge.legendary.core.internal.legendary.arena.event;

import io.github.legendaryforge.legendary.core.api.event.Event;
import java.util.Objects;
import java.util.UUID;

/** Internal signal indicating a player should no longer be treated as an active participant in an arena instance. */
public record ArenaParticipationRevokedEvent(UUID instanceId, UUID playerId) implements Event {
    public ArenaParticipationRevokedEvent {
        Objects.requireNonNull(instanceId, "instanceId");
        Objects.requireNonNull(playerId, "playerId");
    }
}
