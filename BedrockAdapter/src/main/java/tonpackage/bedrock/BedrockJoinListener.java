package tonpackage.bedrock;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * S'execute en priorite LOWEST pour peupler le cache UUID
 * AVANT que le systeme de sync grades/MySQL ne s'execute.
 */
public class BedrockJoinListener implements Listener {

    private final BedrockPlayerManager bedrockManager;
    private final Logger logger;

    public BedrockJoinListener(BedrockPlayerManager bedrockManager, Logger logger) {
        this.bedrockManager = bedrockManager;
        this.logger = logger;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean isBedrock = bedrockManager.isBedrockPlayer(player);
        UUID effectiveUUID = bedrockManager.getEffectiveUUID(player);

        if (isBedrock) {
            logger.info("[Bedrock] " + player.getName()
                    + " connecte via Geyser | UUID effectif: " + effectiveUUID
                    + " | Payment eligible: " + bedrockManager.isEligibleForPayment(player));
        }

        PlayerUUIDCache.put(player.getUniqueId(), effectiveUUID, isBedrock);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        PlayerUUIDCache.remove(event.getPlayer().getUniqueId());
    }
}
