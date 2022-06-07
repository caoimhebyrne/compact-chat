package dev.cbyrne.compactchat.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.gui.screen.Screen;

@Config(name = "compactchat")
public class Configuration implements ConfigData {
    @Excluded
    private static Configuration INSTANCE;

    public boolean infiniteChatHistory = true;
    public boolean clearChatHistoryOnWorldJoin = true;
    public boolean resetCounterOnWorldJoin = false;
    public boolean onlyCompactConsecutiveMessages = false;

    public static Configuration getInstance() {
        if (INSTANCE == null) {
            INSTANCE = AutoConfig.register(Configuration.class, GsonConfigSerializer::new).get();
        }

        return INSTANCE;
    }

    public Screen getScreen(Screen parent) {
        // NOTE: This is not static because we need to register our configuration before getting its screen :P
        return AutoConfig.getConfigScreen(Configuration.class, parent).get();
    }
}
