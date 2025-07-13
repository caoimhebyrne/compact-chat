package dev.caoimhe.compactchat;

// @formatter:off
//#if NEOFORGE
//$$ import me.shedaniel.autoconfig.AutoConfig;
//$$ import net.neoforged.fml.ModContainer;
//$$ import net.neoforged.fml.common.Mod;
//$$ import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//#elseif FABRIC
import net.fabricmc.api.ClientModInitializer;
//#endif

import dev.caoimhe.compactchat.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//#if NEOFORGE
//$$ @Mod("dev_caoimhe_compact_chat")
//$$ public class CompactChatMod {
//$$     public CompactChatMod(final ModContainer modContainer) {
//$$         modContainer.registerExtensionPoint(
//$$             IConfigScreenFactory.class,
//$$             (container, parent) -> AutoConfig.getConfigScreen(Configuration.class, parent).get()
//$$         );
//#else
public class CompactChatMod implements ClientModInitializer {
    public static final Logger LOGGER =  LoggerFactory.getLogger(CompactChatMod.class);

    @Override
    public void onInitializeClient() {
//#endif

        Configuration.initialize();
    }
}
