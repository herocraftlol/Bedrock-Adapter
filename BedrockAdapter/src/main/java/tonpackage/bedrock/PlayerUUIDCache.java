package tonpackage.bedrock;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache en memoire pour eviter d'appeler l'API Floodgate a chaque lookup.
 * Rempli au join, nettoye au quit.
 */
public class PlayerUUIDCache {

    private static final Map<UUID, UUID> effectiveUUIDs = new ConcurrentHashMap<>();
    private static final Map<UUID, Boolean> bedrockFlags = new ConcurrentHashMap<>();

    public static void put(UUID rawUUID, UUID effectiveUUID, boolean isBedrock) {
        effectiveUUIDs.put(rawUUID, effectiveUUID);
        bedrockFlags.put(rawUUID, isBedrock);
    }

    public static UUID getEffectiveUUID(UUID rawUUID) {
        return effectiveUUIDs.getOrDefault(rawUUID, rawUUID);
    }

    public static boolean isBedrock(UUID rawUUID) {
        return bedrockFlags.getOrDefault(rawUUID, false);
    }

    public static void remove(UUID rawUUID) {
        effectiveUUIDs.remove(rawUUID);
        bedrockFlags.remove(rawUUID);
    }
}
