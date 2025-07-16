package dev.caoimhe.compactchat.mixin;

import dev.caoimhe.compactchat.ext.IChatHudExt;
import dev.caoimhe.compactchat.message.MessageManager;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
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

/**
 * The priority is set to the max value integer to ensure that this mixin is applied as late as possible.
 * See GitHub issue #23 for more information.
 */
@Mixin(value = ChatHud.class, priority = Integer.MAX_VALUE)
public abstract class ChatHudMixin implements IChatHudExt {
    @Shadow
    @Final
    private List<ChatHudLine> messages;

    @Shadow
    protected abstract void refresh();

    @Unique
    private final MessageManager messageManager = new MessageManager(this);

    @ModifyVariable(
        method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
        at = @At("HEAD"),
        argsOnly = true
    )
    public Text compactChat$addMessage(final Text message) {
        return this.messageManager.compactMessage(message);
    }

    @Inject(method = "clear", at = @At("HEAD"))
    public void compactChat$clear(boolean clearHistory, CallbackInfo ci) {
        this.messageManager.clear();
    }

    @Override
    public List<ChatHudLine> compactChat$getMessages() {
        return this.messages;
    }

    @Override
    public void compactChat$refreshMessages() {
        this.refresh();
    }
}
