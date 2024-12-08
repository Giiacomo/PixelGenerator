package me.giacomo.minecraft.pixelGenerator;

import me.giacomo.minecraft.pixelGenerator.commands.MainPluginCommand;
import me.giacomo.minecraft.pixelGenerator.events.chat.TabComplete;
import me.giacomo.minecraft.pixelGenerator.events.generator.GeneratorPlaceListener;
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
        manager = new GeneratorManager();
        getLogger().info("Plugin enabled! Version: " + getDescription().getVersion());

        // load config.yml (generate one if not there)
        loadConfiguration();

        // load language.yml (generate one if not there)
        //loadLangFile();


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
        getCommand("template").setExecutor(new MainPluginCommand());
        getCommand("generator").setExecutor(new me.giacomo.minecraft.pixelGenerator.commands.GeneratorCommand(manager));
        // set up tab completion
        getCommand("template").setTabCompleter(new TabComplete());
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new GeneratorPlaceListener(), this);
    }

    // load the config file and apply settings
    public void loadConfiguration() {
        // Prepare config.yml (generate one if not there)
        saveDefaultConfig();  // This saves the config.yml from the JAR to the data folder

        FileConfiguration config = this.getConfig();

        // General settings
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("plugin-prefix"));
        customSetting = config.getBoolean("custom-setting");

        // Put more settings here as needed

        Bukkit.getLogger().info(consolePrefix + "Settings Reloaded from config");
    }


    // load the language file and apply settings
    public void loadLangFile() {

        // load language.yml (generate one if not there)
        // console and IO, instance
        File langFile = new File(getDataFolder(), "language.yml");
        FileConfiguration langFileConfig = new YamlConfiguration();
        if (!langFile.exists()){ Utilities.loadResource(this, "language.yml"); }

        try { langFileConfig.load(langFile); }
        catch (Exception e3) { e3.printStackTrace(); }

        for(String priceString : langFileConfig.getKeys(false)) {
            phrases.put(priceString, langFileConfig.getString(priceString));
        }
    }

    // reload all plugin assets
    public static void reload() {
        getInstance().reloadConfig();
        getInstance().loadConfiguration();
        getInstance().loadLangFile();
        Bukkit.getLogger().info("configuration, values, and language settings reloaded");
    }

    // getters
    public static String getPhrase(String key) {
        return phrases.get(key);
    }
    public static PixelGenerator getInstance() { return instance; }
    public String getVersion() {
        return getDescription().getVersion();
    }
}
