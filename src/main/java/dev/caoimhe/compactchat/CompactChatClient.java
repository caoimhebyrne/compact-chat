package dev.caoimhe.compactchat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.caoimhe.compactchat.config.Configuration;
import dev.caoimhe.compactchat.util.CollectionUtil;
import dev.caoimhe.compactchat.util.FabricLoaderUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.OrderedText;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CompactChatClient implements ClientModInitializer {
    /**
     * @see dev.caoimhe.compactchat.mixin.ChatMessagesMixin
     */
    public static final Cache<Pair<String, Integer>, List<OrderedText>> SPLIT_MESSAGES_CACHE = CacheBuilder.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .build();

    private static final Logger LOGGER = LoggerFactory.getLogger("compact-chat");

    private static final List<String> STARTUP_MESSAGES = CollectionUtil.makeArrayList(
        "Helloooo! Is anyone out there?",
        "My compactor is locked and loaded!",
        "Does anyone know where the bathroom is around here?",
        "Get in the mod loading line punk, {RANDOM_MOD}!",
        "zZz zZz zZ- oh, hey! I'm awake, I'm awake..."
    );

    @Override
    public void onInitializeClient() {
        this.migrateJson5Configuration();

        Configuration.register();

        var message = CollectionUtil.randomFrom(STARTUP_MESSAGES);
        message = message.replace("{RANDOM_MOD}", FabricLoaderUtil.getRandomModName());

        LOGGER.info(message + " (Compact Chat is ready!)");
    }

    /**
     * Migrates the old JSON5 configuration file to the new JSON configuration file.
     * We can't really remove this, because it would break the configuration for users who are updating from 2.0.1 or below.
     */
    private void migrateJson5Configuration() {
        // Exceptions shouldn't really happen (apart from Files#move), but we don't want this to prevent the user from starting their game.
        try {
            var oldFile = FabricLoader.getInstance().getConfigDir().resolve("compact-chat.json5");
            if (!Files.exists(oldFile)) {
                LOGGER.debug("No old configuration file found. Skipping automatic migration.");
                return;
            }

            var newFile = FabricLoader.getInstance().getConfigDir().resolve("compact-chat.json");
            if (Files.exists(newFile)) {
                LOGGER.warn("Both an old (<=2.0.1) and new configuration file (>=2.1.0) exist. Skipping automatic migration.");
                return;
            }

            Files.move(oldFile, newFile);
            LOGGER.info("Successfully migrated old configuration file.");
        } catch (Exception e) {
            LOGGER.error("Failed to migrate old configuration file!", e);
            LOGGER.error("Please manually migrate your old configuration file (config/compact-chat.json5) to the new location (config/compact-chat.json).");
            LOGGER.error("If you don't do this, your configuration will be reset to the default values.");
        }
    }
}