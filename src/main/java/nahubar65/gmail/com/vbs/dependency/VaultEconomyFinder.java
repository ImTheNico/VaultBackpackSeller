package nahubar65.gmail.com.vbs.dependency;

import nahubar65.gmail.com.vbs.vault.VaultHook;

public abstract class VaultEconomyFinder extends PluginDependencyFinder {


    public VaultEconomyFinder() {
        super(15, "Vault");
    }

    @Override
    public boolean searchDependency() {
        return VaultHook.setupEconomy();
    }
}