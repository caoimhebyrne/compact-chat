package dev.cbyrne.compactchat;

import dev.cbyrne.compactchat.config.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import java.util.HashMap;
import java.util.Map;

public class CompactChat implements ModInitializer {
    public static final Map<String, Integer> messageCounters = new HashMap<>();

    @Override
    public void onInitialize() {
        AutoConfig.register(Configuration.class, GsonConfigSerializer::new);
    }
}
