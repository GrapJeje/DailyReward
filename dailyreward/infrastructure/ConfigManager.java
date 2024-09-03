package eu.camonetwork.dailyreward.infrastructure;

import eu.camonetwork.dailyreward.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public abstract class ConfigManager {
    private FileConfiguration customConfig;
    private File customConfigFile = null;
    private String subFolder;
    private String fileName;

    public void reload() {
        if (customConfigFile == null) {
            customConfigFile = new File(Main.instance.getDataFolder(), getFileName());
        }

        if (!customConfigFile.exists()) {
            this.saveDefaultConfig();
        }

        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        InputStream resourceStream = Main.instance.getResource(getFileName());
        if (resourceStream == null) {
            Main.instance.getLogger().severe("Resource stream for " + getFileName() + " is null!");
            return;
        }

        Reader defConfigStream = new InputStreamReader(resourceStream, StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        customConfig.setDefaults(defConfig);
        this.loadConfig();
    }

    public FileConfiguration getConfig() {
        if (customConfig == null) {
            this.reload();
        }
        return customConfig;
    }

    public boolean save() {
        if (customConfig == null || customConfigFile == null) {
            return false;
        }
        try {
            getConfig().save(customConfigFile);
        } catch (IOException ex) {
            Main.instance.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
        this.reload();
        return true;
    }

    public void saveDefaultConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(getFolder(), getFileName());
        }
        if (!customConfigFile.exists()) {
            Main.instance.saveResource(getFileName(), false);
        }
    }

    protected void loadConfig() {

    }

    public File getFolder() {
        return new File(Main.instance.getDataFolder() + this.getSubFolder());
    }

    public String getFileName() {
        return this.getSubFolder() + fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSubFolder() {
        return (subFolder == null ? "" : subFolder + "/");
    }

    public void setSubFolder(String subFolder) {
        this.subFolder = subFolder;
    }
}
