package tonpackage.bedrock;

import org.bukkit.plugin.java.JavaPlugin;

public class BedrockAdapterPlugin extends JavaPlugin {

    private BedrockPlayerManager bedrockManager;

    @Override
    public void onEnable() {
        this.bedrockManager = new BedrockPlayerManager();

        getServer().getPluginManager().registerEvents(
                new BedrockJoinListener(bedrockManager, getLogger()), this
        );

        getLogger().info("BedrockAdapter active - Floodgate detecte.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BedrockAdapter desactive.");
    }

    /**
     * Point d'entree a utiliser depuis ton autre plugin (grades/MySQL)
     * pour recuperer le manager sans dupliquer la logique.
     */
    public BedrockPlayerManager getBedrockManager() {
        return bedrockManager;
    }
}
