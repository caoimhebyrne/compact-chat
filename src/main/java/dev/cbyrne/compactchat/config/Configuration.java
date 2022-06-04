package dev.cbyrne.compactchat.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "compactchat")
public class Configuration implements ConfigData {
    public boolean infiniteChatHistory = true;

    public static Configuration getInstance() {
        return AutoConfig.getConfigHolder(Configuration.class).getConfig();
    }
}
