package tonpackage.bedrock;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

/**
 * Centralise toute la logique de distinction Bedrock / Java.
 * A utiliser depuis le systeme de grades / MySQL / Stripe existant
 * a la place d'un appel direct a player.getUniqueId().
 */
public class BedrockPlayerManager {

    private final FloodgateApi floodgateApi;

    public BedrockPlayerManager() {
        this.floodgateApi = FloodgateApi.getInstance();
    }

    /**
     * Vrai si le joueur est connecte via Geyser/Floodgate (Bedrock).
     */
    public boolean isBedrockPlayer(Player player) {
        return floodgateApi.isFloodgatePlayer(player.getUniqueId());
    }

    /**
     * UUID a utiliser pour les lookups MySQL / grades / Stripe.
     * - Joueur Java classique -> son UUID normal.
     * - Joueur Bedrock lie a un compte Java -> UUID Java lie.
     * - Joueur Bedrock NON lie -> UUID Floodgate (a traiter a part).
     */
    public UUID getEffectiveUUID(Player player) {
        if (!isBedrockPlayer(player)) {
            return player.getUniqueId();
        }

        FloodgatePlayer fp = floodgateApi.getPlayer(player.getUniqueId());
        if (fp != null && fp.isLinked()) {
            return fp.getLinkedPlayer().getJavaUniqueId();
        }

        return player.getUniqueId();
    }

    /**
     * Un joueur Bedrock non lie n'a pas de compte Microsoft standard
     * derriere lui : a exclure des paiements Stripe tant qu'il n'est pas lie.
     */
    public boolean isEligibleForPayment(Player player) {
        if (!isBedrockPlayer(player)) {
            return true;
        }

        FloodgatePlayer fp = floodgateApi.getPlayer(player.getUniqueId());
        return fp != null && fp.isLinked();
    }

    /**
     * Nom d'affichage du joueur (Floodgate peut prefixer le pseudo Bedrock
     * selon la configuration, ex: "." devant le nom).
     */
    public String getDisplayName(Player player) {
        return player.getName();
    }
}
