package me.giacomo.minecraft.pixelGenerator;

import me.giacomo.minecraft.pixelGenerator.db.GeneratorDB;
import me.giacomo.minecraft.pixelGenerator.events.chat.TabComplete;
import me.giacomo.minecraft.pixelGenerator.events.generator.GeneratorBlockListener;
import me.giacomo.minecraft.pixelGenerator.events.generator.GeneratorDamageListener;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorManager;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class PixelGenerator extends JavaPlugin {


    private GeneratorDB generatorDB;
    private static PixelGenerator instance;
    private static GeneratorManager manager;
    // chat messages
    private static Map<String, String> phrases = new HashMap<>();

    // core settings
    public static String prefix = "&c&l[&5&lPixelGenerator&c&l] &8&l"; // generally unchanged unless otherwise stated in config
    public static String consolePrefix = "[PixelGenerator] ";

    // customizable settings
    public static boolean customSetting = false;

    @Override
    public void onEnable() {
        instance = this;
        loadConfiguration();

        try {
            generatorDB = new GeneratorDB(getDataFolder().getAbsolutePath() + "/data.db");
            GeneratorManager.loadAllGenerators();
        } catch (Exception e) {
            getLogger().severe("[PixelGenerator] Could not load db!");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        loadLangFile();

        registerCommands();
        registerEvents();

        // posts confirmation in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been enabled");


    }

    @Override
    public void onDisable() {
        // posts exit message in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been disabled");
    }

    private void registerCommands() {
        getCommand("generator").setExecutor(new me.giacomo.minecraft.pixelGenerator.commands.GeneratorCommand(manager));
        getCommand("generator").setTabCompleter(new TabComplete());
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new GeneratorDamageListener(), this);
        getServer().getPluginManager().registerEvents(new GeneratorBlockListener(), this);
    }

    public GeneratorDB getGeneratorDB() {
        return generatorDB;
    }

    public void loadConfiguration() {
        saveDefaultConfig();

        FileConfiguration config = this.getConfig();
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("plugin-prefix"));

        getLogger().info(consolePrefix + "Settings Reloaded from config");
    }


    public void loadLangFile() {
        File langFile = new File(getDataFolder(), "lang.yml");
        FileConfiguration langFileConfig = new YamlConfiguration();
        if (!langFile.exists()){ Utilities.loadResource(this, "lang.yml"); }

        try { langFileConfig.load(langFile); }
        catch (Exception e3) { e3.printStackTrace(); }

        for(String s : langFileConfig.getKeys(false)) {
            phrases.put(s, langFileConfig.getString(s));
        }
    }

    public static void reload() {
        getInstance().reloadConfig();
        getInstance().loadConfiguration();
        getInstance().loadLangFile();
        Bukkit.getLogger().info("configuration, values, and language settings reloaded");
    }

    public static String getPhrase(String key) {
        return phrases.get(key);
    }
    public static PixelGenerator getInstance() { return instance; }
    public String getVersion() {
        return getDescription().getVersion();
    }
}
