package dev.cbyrne.compactchat;

import com.mojang.logging.LogUtils;
import dev.cbyrne.compactchat.config.Configuration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class CompactChat implements ModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Map<String, Integer> MESSAGE_COUNTERS = new HashMap<>();

    @Override
    public void onInitialize() {
        ClientLoginConnectionEvents.INIT.register(this::resetMessageCounters);
    }

    private void resetMessageCounters(ClientLoginNetworkHandler handler, MinecraftClient client) {
        if (!Configuration.getInstance().resetCounterOnWorldJoin) return;

        LOGGER.info("Clearing message counters of length " + MESSAGE_COUNTERS.size() + " because the client is joining a new world!");
        MESSAGE_COUNTERS.clear();
    }
}
