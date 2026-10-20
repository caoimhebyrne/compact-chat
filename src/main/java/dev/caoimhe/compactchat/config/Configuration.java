package dev.caoimhe.compactchat.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.gui.screen.Screen;

// @formatter:off
//#if MC >= 26.1
//$$ import me.shedaniel.autoconfig.AutoConfigClient;
//#endif
// @formatter:on

import java.util.List;

@Config(name = "compact-chat")
public class Configuration implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public int maximumOccurrences = 100;

    @ConfigEntry.Gui.Tooltip
    public int maxTrackedMessages = 500;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    @ConfigEntry.Gui.Tooltip
    public int ignoreFirstCharactersCount = 0;

    @ConfigEntry.Gui.Tooltip
    public boolean onlyCompactConsecutiveMessages = false;

    @ConfigEntry.Gui.Tooltip
    public boolean ignoreCommonSeparators = true;

    @ConfigEntry.Gui.Tooltip
    public List<String> commonSeparators = List.of("-----", "======");

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

    /**
     * Creates the configuration screen.
     * Must be called after {@link Configuration#initialize()}.
     */
    public static Screen createScreen(Screen parent) {
        //#if MC >= 26.1
        //$$ return AutoConfigClient.getConfigScreen(Configuration.class, parent).get();
        //#else
        return AutoConfig.getConfigScreen(Configuration.class, parent).get();
        //#endif
    }
}
