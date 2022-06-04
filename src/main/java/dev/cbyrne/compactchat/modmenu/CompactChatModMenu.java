package dev.cbyrne.compactchat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.cbyrne.compactchat.config.Configuration;
import me.shedaniel.autoconfig.AutoConfig;

public class CompactChatModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(Configuration.class, parent).get();
    }
}