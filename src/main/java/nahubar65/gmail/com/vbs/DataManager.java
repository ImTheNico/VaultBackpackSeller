package nahubar65.gmail.com.vbs;

import nahubar65.gmail.com.backpacksystem.core.configuration.Configuration;
import nahubar65.gmail.com.backpacksystem.core.configuration.manager.ConfigurationManager;
import nahubar65.gmail.com.vbs.backpack.BackpackEnhancer;
import nahubar65.gmail.com.vbs.backpack.SimpleBackpackEnhancer;

public class DataManager {

    private final Configuration configuration;

    private final Configuration messages;

    private final ConfigurationManager configurationManager;

    private final BackpackEnhancer backpackEnhancer;

    public DataManager(VaultBackpackSeller vaultBackpackSeller) {
        this.configurationManager = ConfigurationManager.newManager(vaultBackpackSeller, vaultBackpackSeller.getFolder());
        this.configuration = configurationManager.createIfNotExists("config");
        this.messages = configurationManager.createIfNotExists("messages");
        this.backpackEnhancer = new SimpleBackpackEnhancer(configuration);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Configuration getMessages() {
        return messages;
    }

    public BackpackEnhancer getBackpackEnhancer() {
        return backpackEnhancer;
    }

    public void reload() {
        configurationManager.reload();
        this.backpackEnhancer.reload();
    }
}