package nahubar65.gmail.com.vbs.backpack;

import nahubar65.gmail.com.backpacksystem.api.Backpack;
import nahubar65.gmail.com.backpacksystem.core.configuration.Configuration;
import nahubar65.gmail.com.vbs.vault.VaultHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class SimpleBackpackEnhancer implements BackpackEnhancer {

    private final Economy economy;

    private Map<Integer, Double> prices;

    private Configuration configuration;

    public SimpleBackpackEnhancer(Configuration configuration) {
        economy = VaultHook.getEconomy();

        this.configuration = configuration;

        reload();
    }

    @Override
    public double getPrice(int level) {
        Double price = prices.get(level);

        if (price == null) {
            price = defaultPrices().get(level);
        }

        return price != null ? price : 0;
    }

    @Override
    public boolean upgrade(Backpack backpack) {
        int toUpgrade = backpack.getLevel().getLevel() + 1;
        if (toUpgrade > 6)
            return false;

        double price = getPrice(toUpgrade);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(backpack.getOwner());

        if (offlinePlayer != null) {
            if (economy.has(offlinePlayer, price)) {
                economy.withdrawPlayer(offlinePlayer, price);
                backpack.upgrade(1);
                return true;
            }
        }
        return false;
    }

    @Override
    public void reload() {
        ConfigurationSection configurationSection = configuration.getConfigurationSection("backpack-enhancer.levels");

        build(configurationSection);
    }

    private void build(ConfigurationSection configurationSection) {
        if (configurationSection != null) {
            this.prices = new HashMap<>();

            int count = 1;
            for (String key : configurationSection.getKeys(false)) {
                if (count >= 6)
                    break;

                ConfigurationSection configurationSection1 = configurationSection.getConfigurationSection(key);
                double price = configurationSection1.getDouble("price");
                prices.put(count, price);
                count++;
            }

        } else {
            this.prices = defaultPrices();
        }
    }
}