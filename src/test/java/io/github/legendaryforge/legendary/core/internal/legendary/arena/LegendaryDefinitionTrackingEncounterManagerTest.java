package io.github.legendaryforge.legendary.core.internal.legendary.arena;

import io.github.legendaryforge.legendary.core.api.encounter.EncounterContext;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterDefinition;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterInstance;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterKey;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterManager;
import io.github.legendaryforge.legendary.core.api.encounter.EndReason;
import io.github.legendaryforge.legendary.core.api.encounter.JoinResult;
import io.github.legendaryforge.legendary.core.api.encounter.ParticipationRole;
import io.github.legendaryforge.legendary.core.api.encounter.SpectatorPolicy;
import io.github.legendaryforge.legendary.core.api.encounter.EncounterAccessPolicy;
import io.github.legendaryforge.legendary.core.api.id.ResourceId;
import io.github.legendaryforge.legendary.core.api.legendary.definition.LegendaryEncounterDefinition;
import java.lang.reflect.Proxy;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class LegendaryDefinitionTrackingEncounterManagerTest {

    @Test
    void tracksLegendaryDefinitionIdsOnCreate() {
        Set<ResourceId> ids = ConcurrentHashMap.newKeySet();

        EncounterManager delegate = new EncounterManager() {
            @Override
            public EncounterInstance create(EncounterDefinition definition, EncounterContext context) {
                return proxyEncounterInstance();
            }

            @Override
            public JoinResult join(UUID playerId, EncounterInstance instance, ParticipationRole role) {
                return null;
            }

            @Override
            public void leave(UUID playerId, EncounterInstance instance) {}

            @Override
            public void end(EncounterInstance instance, EndReason reason) {}

            @Override
            public Optional<EncounterInstance> byInstanceId(UUID instanceId) {
                return Optional.empty();
            }

            @Override
            public Optional<EncounterInstance> byKey(EncounterKey key) {
                return Optional.empty();
            }
        };

        LegendaryDefinitionTrackingEncounterManager mgr =
                new LegendaryDefinitionTrackingEncounterManager(delegate, ids);

        ResourceId normalId = ResourceId.parse("test:normal_encounter");
        ResourceId legendaryId = ResourceId.parse("test:legendary_encounter");

        EncounterDefinition normalDef = proxyEncounterDefinition(EncounterDefinition.class, normalId);
        LegendaryEncounterDefinition legendaryDef = proxyEncounterDefinition(LegendaryEncounterDefinition.class, legendaryId);

        mgr.create(normalDef, null);
        assertFalse(mgr.isLegendary(normalId));

        mgr.create(legendaryDef, null);
        assertTrue(mgr.isLegendary(legendaryId));
    }

    private static EncounterInstance proxyEncounterInstance() {
        return (EncounterInstance) Proxy.newProxyInstance(
                EncounterInstance.class.getClassLoader(),
                new Class<?>[] { EncounterInstance.class },
                (proxy, method, args) -> {
                    Class<?> rt = method.getReturnType();
                    if (rt.equals(UUID.class)) return UUID.randomUUID();
                    if (rt.equals(Optional.class)) return Optional.empty();
                    if (rt.equals(int.class)) return 0;
                    if (rt.equals(boolean.class)) return false;
                    return null;
                });
    }

    private static <T> T proxyEncounterDefinition(Class<T> type, ResourceId id) {
        return type.cast(Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class<?>[] { type },
                (proxy, method, args) -> {
                    return switch (method.getName()) {
                        case "id" -> id;
                        case "displayName" -> "test";
                        case "accessPolicy" -> EncounterAccessPolicy.PUBLIC;
                        case "spectatorPolicy" -> SpectatorPolicy.ALLOW_VIEW_ONLY;
                        case "maxParticipants" -> 0;
                        case "maxSpectators" -> 0;
                        default -> null;
                    };
                }));
    }
}
