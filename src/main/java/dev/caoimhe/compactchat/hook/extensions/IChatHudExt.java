package dev.caoimhe.compactchat.hook.extensions;

import net.minecraft.client.gui.hud.ChatHudLine;

import java.util.List;

/**
 * Bootleg Kotlin extension functions...
 * These methods are prefixed with `compactchat$` to avoid mixin conflicts
 */
public interface IChatHudExt {
    List<ChatHudLine> compactchat$getMessages();
    void compactchat$refreshMessages();
    void compactchat$clear();
}
