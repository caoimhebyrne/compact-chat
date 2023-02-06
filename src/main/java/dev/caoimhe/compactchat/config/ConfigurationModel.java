package dev.caoimhe.compactchat.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Hook;
import io.wispforest.owo.config.annotation.Modmenu;

@SuppressWarnings("unused")
@Modmenu(modId = "compact-chat")
@Config(name = "compact-chat", wrapperName = "Configuration")
public class ConfigurationModel {
    /**
     * If enabled, only messages which have been sent one after the other will be compacted.
     */
    @Hook
    public boolean onlyCompactConsecutiveMessages = false;
}
