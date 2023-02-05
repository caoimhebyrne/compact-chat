package dev.caoimhe.compactchat.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ActionResult;

@Config(name = "compact-chat")
public class Configuration implements ConfigData {
    @Excluded
    private static Configuration INSTANCE;

    /**
     * If enabled, only messages which have been sent one after the other will be compacted.
     */
    @Tooltip(count = 13)
    public boolean onlyCompactConsecutiveMessages = false;

    public static Configuration instance() {
        if (INSTANCE == null) {
            var holder = AutoConfig.register(Configuration.class, GsonConfigSerializer::new);
            holder.registerSaveListener(Configuration::onSave);

            INSTANCE = holder.get();
        }

        return INSTANCE;
    }

    /**
     * Executed whenever our configuration is saved
     */
    private static ActionResult onSave(
        ConfigHolder<Configuration> configurationConfigHolder,
        Configuration configuration
    ) {
        // We want to clear the chat to prevent issues with consecutive messages.
        // TODO: Only do this if onlyCompactConsecutiveMessages has changed!
        MinecraftClient.getInstance().inGameHud.getChatHud().clear(false);

        return ActionResult.SUCCESS;
    }

    /**
     * Returns a configuration screen for this class
     */
    public Screen getScreen(Screen screen) {
        return AutoConfig.getConfigScreen(Configuration.class, screen).get();
    }
}
