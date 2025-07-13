package dev.caoimhe.compactchat.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

@Config(name = "compact-chat")
public class Configuration implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean onlyCompactConsecutiveMessages = false;

    @ConfigEntry.Gui.Tooltip
    public boolean ignoreCommonSeparators = true;

    @ConfigEntry.Gui.Tooltip
    public int maximumOccurrences = 100;

    /**
     * Initializes the configuration.
     */
    public static void initialize() {
        AutoConfig.register(Configuration.class, GsonConfigSerializer::new);
    }

    /**
     * Retrieves an instance of the {@link Configuration}.
     * Must be called after {@link Configuration#initialize()}.
     */
    public static Configuration instance() {
        return AutoConfig.getConfigHolder(Configuration.class).getConfig();
    }
}
