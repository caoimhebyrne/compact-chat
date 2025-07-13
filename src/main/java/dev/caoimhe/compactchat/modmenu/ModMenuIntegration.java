package dev.caoimhe.compactchat.modmenu;

//#if FABRIC

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.caoimhe.compactchat.config.Configuration;
import me.shedaniel.autoconfig.AutoConfig;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(Configuration.class, parent).get();
    }
}

//#endif
