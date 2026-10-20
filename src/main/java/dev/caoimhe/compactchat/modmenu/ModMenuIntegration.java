package dev.caoimhe.compactchat.modmenu;

//#if FABRIC

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.caoimhe.compactchat.config.Configuration;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return Configuration::createScreen;
    }
}

//#endif
