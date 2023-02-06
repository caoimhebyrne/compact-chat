package dev.caoimhe.compactchat.mixin;

import dev.caoimhe.compactchat.hook.ChatHudHook;
import dev.caoimhe.compactchat.hook.extensions.IChatHudExt;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin implements IChatHudExt {
    @Unique
    private final ChatHudHook compactchat$hook = new ChatHudHook(this);

    @Shadow
    @Final
    private List<ChatHudLine> messages;

    @Shadow
    public abstract void reset();

    @Shadow
    public abstract void clear(boolean clearHistory);

    @ModifyVariable(
        method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V",
        at = @At("HEAD"),
        argsOnly = true
    )
    private Text compactchat$compactChatMessage(Text message, Text parameterMessage, MessageSignatureData data, int ticks, MessageIndicator indicator, boolean refreshing) {
        // If we're refreshing, we have probably already modified the message, therefore we don't want to do anything.
        if (refreshing) {
            return message;
        }

        return this.compactchat$hook.compactChatMessage(message);
    }

    @Inject(method = "clear", at = @At("RETURN"))
    private void compactchat$onClear(boolean clearHistory, CallbackInfo ci) {
        this.compactchat$hook.reset();
    }

    @Override
    public List<ChatHudLine> compactchat$getMessages() {
        return this.messages;
    }

    @Override
    public void compactchat$refreshMessages() {
        this.reset();
    }

    @Override
    public void compactchat$clear() {
        this.clear(false);
    }
}
