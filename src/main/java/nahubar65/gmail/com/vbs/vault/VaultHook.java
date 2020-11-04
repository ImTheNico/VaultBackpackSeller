package nahubar65.gmail.com.vbs.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

public class VaultHook {

    private static Economy economy;

    private VaultHook() {
        throw new UnsupportedOperationException("This class cannot be instanced.");
    }

    public static boolean setupEconomy() {
        ServicesManager servicesManager = Bukkit.getServicesManager();
        RegisteredServiceProvider<Economy> economyRegisteredServiceProvider = servicesManager.getRegistration(Economy.class);

        if (economyRegisteredServiceProvider != null)
            economy = economyRegisteredServiceProvider.getProvider();

        return economy != null;
    }

    public static Economy getEconomy() {
        return economy;
    }
}