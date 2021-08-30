package dev.cbyrne.compactchat;

import dev.cbyrne.compactchat.config.Configuration;
import net.fabricmc.api.ModInitializer;

public class CompactChat implements ModInitializer {
    @Override
    public void onInitialize() {
        Configuration.INSTANCE.load();
    }
}
