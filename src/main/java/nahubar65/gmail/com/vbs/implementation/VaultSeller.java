package nahubar65.gmail.com.vbs.implementation;

import nahubar65.gmail.com.backpacksystem.api.BackpackManager;
import nahubar65.gmail.com.backpacksystem.plugin.service.BackpackService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;

public final class VaultSeller {

    private final Economy economy;

    private final BackpackManager backpackManager;

    public VaultSeller(Economy economy) {
        this.economy = economy;
        this.backpackManager = BackpackService.getBackpackManager();
    }

    public boolean giveBackpack(OfflinePlayer offlinePlayer, double money) {
        if (economy.has(offlinePlayer, money)) {
            economy.withdrawPlayer(offlinePlayer, money);
            backpackManager.giveBackpack(offlinePlayer);
            return true;
        }

        return false;
    }
}