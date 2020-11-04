package nahubar65.gmail.com.vbs;

import nahubar65.gmail.com.backpacksystem.core.configuration.Configuration;
import nahubar65.gmail.com.backpacksystem.plugin.loader.CommandLoader;
import nahubar65.gmail.com.vbs.command.VaultBackpackCommand;
import nahubar65.gmail.com.vbs.dependency.VaultEconomyFinder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class VaultBackpackSeller extends JavaPlugin {

    private DataManager dataManager;

    @Override
    public void onEnable() {
        new VaultEconomyFinder() {
            @Override
            public void onFound() {
                dataManager = new DataManager(VaultBackpackSeller.this);
                Configuration configuration = dataManager.getConfiguration();
                List<String> names = configuration.getStringList("command.names", "vaultbackpackseller", "vbps", "vaultbps");

                CommandLoader commandLoader = new CommandLoader("vaultbackpackseller",
                        new VaultBackpackCommand(configuration, dataManager.getMessages(), dataManager.getBackpackEnhancer(), names.toArray(new String[0])));
                commandLoader.load();
            }

            @Override
            public void onFail() {
                Bukkit.getPluginManager().disablePlugin(VaultBackpackSeller.this);
            }

            @Override
            public boolean searchDependency() {
                return super.searchDependency() && Bukkit.getPluginManager().getPlugin("BackpackSystem") != null;
            }
        }.search(this);
    }

    public File getFolder() {
        File file = getDataFolder();
        if (!file.exists())
            file.mkdirs();

        return file;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}