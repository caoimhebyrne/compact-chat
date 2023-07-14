package dev.caoimhe.compactchat.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.util.ActionResult;

@Config(name = "compact-chat")
public class Configuration implements ConfigData {
    @Excluded
    private static ConfigHolder<Configuration> configHolder;

    /**
     * If enabled, only messages which have been sent one after the other will be compacted.
     */
    @Tooltip
    public boolean onlyCompactConsecutiveMessages = false;

    /**
     * If enabled, common separators like "--------" will not be compacted
     */
    @Tooltip
    public boolean ignoreCommonSeparators = true;

    public static void register() {
        if (configHolder != null) {
            throw new IllegalStateException("Configuration has already been initialized!");
        }

        configHolder = AutoConfig.register(Configuration.class, GsonConfigSerializer::new);
    }

    public static Configuration getInstance() {
        if (configHolder == null) {
            throw new IllegalStateException("Configuration has not been initialized yet!");
        }

        return configHolder.getConfig();
    }

    public void onSave(Runnable action) {
        configHolder.registerSaveListener((holder, config) -> {
            action.run();
            return ActionResult.SUCCESS;
        });
    }
}
