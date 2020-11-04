package nahubar65.gmail.com.vbs.dependency;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public abstract class PluginDependencyFinder implements Runnable {

    private int taskID;

    private int cooldown;

    private String dependencyName;

    private JavaPlugin plugin;

    public PluginDependencyFinder(int cooldown, String dependencyName) {
        this.cooldown = cooldown;
        this.dependencyName = dependencyName;
    }

    public abstract boolean searchDependency();

    public abstract void onFound();

    public abstract void onFail();

    public void search(JavaPlugin plugin){
        this.plugin = plugin;
        Bukkit.getLogger().log(Level.INFO, plugin.getDescription().getName()+" Searching dependency "+dependencyName+"...");
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 20);
    }

    @Override
    public void run() {
        if (searchDependency()) {
            Bukkit.getLogger().log(Level.WARNING, plugin.getDescription().getName()+" Dependency "+dependencyName+" found!");
            Bukkit.getScheduler().cancelTask(taskID);
            onFound();
        } else if (cooldown == 0) {
            Bukkit.getLogger().log(Level.WARNING, plugin.getDescription().getName()+" Dependency "+dependencyName+" not found.");
            Bukkit.getScheduler().cancelTask(taskID);
        }
        cooldown--;
    }
}