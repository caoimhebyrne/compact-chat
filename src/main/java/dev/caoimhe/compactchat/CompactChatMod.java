package dev.caoimhe.compactchat;

// @formatter:off
//#if NEOFORGE
//$$ import net.neoforged.bus.api.IEventBus;
//$$ import net.neoforged.fml.common.Mod;
//#elseif FABRIC
import net.fabricmc.api.ClientModInitializer;
//#endif

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//#if NEOFORGE
//$$ @Mod("dev_caoimhe_compact_chat")
//$$ public class CompactChatMod {
//$$     public CompactChatMod(final IEventBus modEventBus) {
//#else
public class CompactChatMod implements ClientModInitializer {
    public static final Logger LOGGER =  LoggerFactory.getLogger(CompactChatMod.class);

    @Override
    public void onInitializeClient() {
//#endif
    }
}
