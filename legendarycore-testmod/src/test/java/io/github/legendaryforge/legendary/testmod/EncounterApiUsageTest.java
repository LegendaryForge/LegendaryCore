package io.github.legendaryforge.legendary.testmod;

import io.github.legendaryforge.legendary.core.api.encounter.*;
import io.github.legendaryforge.legendary.core.api.id.ResourceId;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Compile-time validation of the Encounter API from a consumer module.
 *
 * <p>This test does not execute encounter logic. Its sole purpose is to ensure
 * external mods can implement and interact with the encounter model using only
 * public LegendaryCore APIs.</p>
 */
public class EncounterApiUsageTest {

    /**
     * Example consumer-defined encounter definition.
     */
    private static final class TestEncounterDefinition implements EncounterDefinition {

        @Override
        public ResourceId id() {
            return ResourceId.of("legendarytest", "test_encounter");
        }

        @Override
        public String displayName() {
            return "Test Encounter";
        }

        @Override
        public EncounterAccessPolicy accessPolicy() {
            return EncounterAccessPolicy.PARTY_ONLY;
        }

        @Override
        public SpectatorPolicy spectatorPolicy() {
            return SpectatorPolicy.ALLOW_VIEW_ONLY;
        }

        @Override
        public int maxParticipants() {
            return 5;
        }

        @Override
        public int maxSpectators() {
            return 0;
        }
    }

    /**
     * This method is never invoked. It exists solely to typecheck consumer-side
     * usage of the public encounter APIs.
     */
    @SuppressWarnings({"unused"})
    private static void compileTimeOnly(EncounterManager manager) {

        EncounterDefinition definition = new TestEncounterDefinition();

        EncounterContext context = new EncounterContext() {
            @Override
            public Optional<UUID> partyId() {
                return Optional.empty();
            }

            @Override
            public ResourceId worldId() {
                return ResourceId.of("legendarytest", "test_world");
            }

            @Override
            public Map<String, Object> metadata() {
                return Map.of("difficulty", "normal");
            }
        };

        EncounterInstance instance = manager.create(definition, context);

        UUID playerId = UUID.randomUUID();

        manager.join(playerId, instance, ParticipationRole.PARTICIPANT);
        manager.join(UUID.randomUUID(), instance, ParticipationRole.SPECTATOR);

        manager.leave(playerId, instance);
        manager.end(instance, EndReason.COMPLETED);

        manager.byInstanceId(instance.instanceId());

        // Instance inspection
        instance.definition();
        instance.state();
        Set<UUID> participants = instance.participants();
        Set<UUID> spectators = instance.spectators();
    }

    @Test
    void encounterApiIsConsumable() {
        // If this test module compiles, the encounter API is consumable.
        assertTrue(true);
    }
}
