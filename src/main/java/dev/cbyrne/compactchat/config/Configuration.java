package dev.cbyrne.compactchat.config;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    public static final Configuration INSTANCE = new Configuration();

    private final Logger logger = LogManager.getLogger("CompactChat: Configuration");
    private final File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "compactchat.properties");

    public boolean infiniteChatHistory = true;

    public void load() {
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
                save();

                return;
            }

            var properties = new Properties();
            properties.load(new FileInputStream(configFile));

            this.infiniteChatHistory = Boolean.parseBoolean(properties.getProperty("infiniteChatHistory"));
        } catch (IOException e) {
            logger.info("Failed to read \"" + configFile.getName() + "\"", e);
        }
    }

    public void save() {
        var properties = new Properties();
        properties.setProperty("infiniteChatHistory", String.valueOf(infiniteChatHistory));

        try {
            properties.store(new FileOutputStream(configFile), "CompactChat Properties File");
        } catch (IOException e) {
            logger.info("Failed to save \"" + configFile.getName() + "\"", e);
        }
    }
}
