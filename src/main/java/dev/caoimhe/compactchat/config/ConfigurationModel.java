package dev.caoimhe.compactchat.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@SuppressWarnings("unused")
@Modmenu(modId = "compact-chat")
@Config(name = "compact-chat", wrapperName = "Configuration", defaultHook = true)
public class ConfigurationModel {
    /**
     * If enabled, only messages which have been sent one after the other will be compacted.
     */
    public boolean onlyCompactConsecutiveMessages = false;

    /**
     * If enabled, common separators like "--------" will not be compacted
     */
    public boolean ignoreCommonSeparators = true;
}
