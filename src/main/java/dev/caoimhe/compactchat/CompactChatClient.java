package dev.caoimhe.compactchat;

import dev.caoimhe.compactchat.util.CollectionUtil;
import dev.caoimhe.compactchat.util.FabricLoaderUtil;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CompactChatClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("compact-chat");

    private static final List<String> STARTUP_MESSAGES = CollectionUtil.makeArrayList(
        "Helloooo! Is anyone out there?",
        "My compactor is locked and loaded!",
        "Does anyone know where the bathroom is around here?",
        "Get in the mod loading line punk, {RANDOM_MOD}!",
        "zZz zZz zZ- oh, hey! I'm awake, I'm awake..."
    );

    @Override
    public void onInitializeClient() {
        var message = CollectionUtil.randomFrom(STARTUP_MESSAGES);
        message = message.replace("{RANDOM_MOD}", FabricLoaderUtil.getRandomModName());

        LOGGER.info(message + " (Compact Chat is ready!)");
    }
}