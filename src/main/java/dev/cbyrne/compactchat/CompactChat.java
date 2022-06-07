package dev.cbyrne.compactchat;

import com.mojang.logging.LogUtils;
import dev.cbyrne.compactchat.config.Configuration;
import dev.cbyrne.compactchat.message.CompactedMessage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class CompactChat implements ModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Set<CompactedMessage> COMPACTED_MESSAGES = new HashSet<>();

    @Override
    public void onInitialize() {
        ClientLoginConnectionEvents.INIT.register(this::resetMessageCounters);
    }

    private void resetMessageCounters(ClientLoginNetworkHandler handler, MinecraftClient client) {
        if (!Configuration.getInstance().resetCounterOnWorldJoin) return;

        LOGGER.info("Clearing message counters of length " + COMPACTED_MESSAGES.size() + " because the client is joining a new world!");
        COMPACTED_MESSAGES.clear();
    }
}
