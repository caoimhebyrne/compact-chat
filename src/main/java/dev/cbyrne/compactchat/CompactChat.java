package dev.cbyrne.compactchat;

import dev.cbyrne.compactchat.config.Configuration;
import net.fabricmc.api.ModInitializer;

import java.util.HashMap;
import java.util.Map;

public class CompactChat implements ModInitializer {
    public static final Map<String, Integer> messageCounters = new HashMap<>();

    @Override
    public void onInitialize() {
        Configuration.INSTANCE.load();
    }
}
