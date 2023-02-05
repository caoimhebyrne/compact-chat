package dev.caoimhe.compactchat.hook.extensions;

import dev.caoimhe.compactchat.core.ChatMessage;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Bootleg Kotlin extension functions
 */
public interface IChatHudExt {
    List<ChatHudLine> getMessages();
    void refreshMessages();
}
