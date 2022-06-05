package dev.cbyrne.compactchat.mixin;

import dev.cbyrne.compactchat.config.Configuration;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;clear(Z)V"), method = "clear", cancellable = true)
    private void compactChat$optionallyClearChatHistory(CallbackInfo ci) {
        if (!Configuration.getInstance().clearChatHistoryOnWorldJoin) {
            ci.cancel();
        }
    }
}
