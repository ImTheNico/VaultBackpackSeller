package nahubar65.gmail.com.vbs.command;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import nahubar65.gmail.com.backpacksystem.api.Backpack;
import nahubar65.gmail.com.backpacksystem.api.BackpackManager;
import nahubar65.gmail.com.backpacksystem.core.configuration.Configuration;
import nahubar65.gmail.com.backpacksystem.core.message.Message;
import nahubar65.gmail.com.backpacksystem.core.message.MessageFactory;
import nahubar65.gmail.com.backpacksystem.core.texdecorator.TextDecorator;
import nahubar65.gmail.com.backpacksystem.plugin.messages.ConfigurationMessages;
import nahubar65.gmail.com.backpacksystem.plugin.service.BackpackService;
import nahubar65.gmail.com.vbs.VaultBackpackSeller;
import nahubar65.gmail.com.vbs.backpack.BackpackEnhancer;
import nahubar65.gmail.com.vbs.implementation.VaultSeller;
import nahubar65.gmail.com.vbs.vault.VaultHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

public class VaultBackpackCommand implements CommandClass, Command {

    private final String[] name;

    private final Configuration messages;

    private final Configuration configuration;

    private final BackpackManager backpackManager;

    private final BackpackEnhancer backpackEnhancer;

    private final VaultSeller vaultSeller;

    private final Economy economy;

    private final VaultBackpackSeller vaultBackpackSeller;

    public VaultBackpackCommand(Configuration configuration, Configuration messages, BackpackEnhancer backpackEnhancer, String... name) {
        this.name = name;
        this.backpackManager = BackpackService.getBackpackManager();
        this.messages = messages;
        this.configuration = configuration;
        this.backpackEnhancer = backpackEnhancer;
        this.economy = VaultHook.getEconomy();
        this.vaultSeller = new VaultSeller(economy);
        this.vaultBackpackSeller = JavaPlugin.getPlugin(VaultBackpackSeller.class);
    }

    @Command(names = "")
    public boolean runMainCommand(CommandSender commandSender) {
        Message<List<String>> listMessage = MessageFactory.newStringListMessage(messages.getStringList(
                "command-help", "&7====[&aVaultBackpackSeller&7]====", "&b/vaultbackpackseller upgrade", "&b/vaultbackpackseller upgrade-price", "&b/vaultbackpackseller buy", "&7======================"));
        listMessage.sendTo(commandSender);
        return true;
    }

    @Command(names = "upgrade")
    public boolean upgrade(CommandSender commandSender) {
        if (checkSender(commandSender)) {
            Player player = (Player) commandSender;
            Optional<Backpack> optionalBackpack = backpackManager.getBackpack(player);
            Message<String> backpackIsInTheMaxLevel = ConfigurationMessages.yourBackpackIsInTheMaxLevel(
                    "your-backpack-is-in-the-max-level", messages);
            if (optionalBackpack.isPresent()) {
                Backpack backpack = optionalBackpack.get();
                if (backpack.isInMaxLevel()) {
                    backpackIsInTheMaxLevel.sendTo(player);
                    return true;
                }

                Message<String> stringMessage;
                if (backpackEnhancer.upgrade(backpack)) {
                     stringMessage = MessageFactory.newStringMessage(
                            messages.getString("backpack-upgraded-message", "&aYour backpack was upgraded to level %level%"));
                    stringMessage.replaceString("%level%", backpack.getLevel().getLevel() + "");
                } else {
                    stringMessage = MessageFactory.newStringMessage(
                            messages.getString("insufficient-money-message", "&cYou need $%needed% to buy this.")
                    );
                    stringMessage.replaceString("%needed%", backpackEnhancer.getPrice(backpack.getLevel().getLevel() + 1) + "");
                }

                stringMessage.sendTo(player);
            } else {
                ConfigurationMessages.doesNotHaveBackpack("you-need-a-backpack-message", messages).sendTo(player);
            }
        }
        return true;
    }

    @Command(names = "buy")
    public boolean buy(CommandSender commandSender) {
        if (checkSender(commandSender)) {
            Player player = (Player) commandSender;
            Optional<Backpack> optionalBackpack = backpackManager.getBackpack(player);
            if (optionalBackpack.isPresent()) {
                MessageFactory.newStringMessage(
                        messages.getString("already-have-backpack", "&cYou already have a backpack.")
                ).sendTo(player);
            } else {
                Message<String> stringMessage;

                double price = configuration.getDouble("backpack-price", 50000);

                if (vaultSeller.giveBackpack(player, price)) {
                    stringMessage = MessageFactory.newStringMessage(messages.getString("buy-successful-message", "&aBuy successful."));
                } else {
                    stringMessage = MessageFactory.newStringMessage(
                            messages.getString("insufficient-money-message", "&cYou need %needed% to buy this."));
                    stringMessage.replaceString("%money%", economy.getBalance(player) + "");
                }
                stringMessage.sendTo(player);
            }
        }
        return true;
    }

    @Command(names = "reload-data")
    public boolean reload(CommandSender commandSender) {
        if (!checkPermissions(commandSender, "reload-data"))
            return true;

        this.vaultBackpackSeller.getDataManager().reload();
        commandSender.sendMessage(TextDecorator.color("&aData reloaded successfully."));
        return true;
    }

    private boolean checkSender(CommandSender commandSender) {
        boolean b = commandSender instanceof Player;
        if (!b)
            commandSender.sendMessage(TextDecorator.color("&cOnly players can execute this command."));
        return b;
    }

    @Override
    public String[] names() {
        return name;
    }

    @Override
    public String desc() {
        return "";
    }

    @Override
    public String permission() {
        return "";
    }

    @Override
    public String permissionMessage() {
        return "";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return VaultBackpackCommand.class;
    }

    private boolean checkPermissions(CommandSender commandSender, String permission) {
        Player player = (Player)commandSender;
        if (!player.hasPermission("vaultbackpackseller.cmd." + permission)) {
            Message<String> message = ConfigurationMessages.insufficientPermission("insufficient-permission-message", this.messages);
            message.sendTo(commandSender);
            return false;
        } else {
            return true;
        }
    }
}