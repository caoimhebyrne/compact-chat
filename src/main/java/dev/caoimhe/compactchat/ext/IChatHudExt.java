package dev.caoimhe.compactchat.ext;

import net.minecraft.client.gui.hud.ChatHudLine;

import java.util.List;

/**
 * An interface to be implemented by {@link net.minecraft.client.gui.hud.ChatHud} at runtime.
 */
public interface IChatHudExt {
    List<ChatHudLine> compactChat$getMessages();
    void compactChat$refreshMessages();
}
