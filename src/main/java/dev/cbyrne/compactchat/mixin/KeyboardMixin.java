package dev.cbyrne.compactchat.mixin;

import dev.cbyrne.compactchat.CompactChat;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "processF3", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/hud/ChatHud;clear(Z)V"))
    private void compactChat$resetMessageCounters(int key, CallbackInfoReturnable<Boolean> cir) {
        CompactChat.MESSAGE_COUNTERS.clear();
    }
}
