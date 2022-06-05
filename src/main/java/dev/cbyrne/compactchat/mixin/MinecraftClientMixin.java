package dev.cbyrne.compactchat.mixin;

import dev.cbyrne.compactchat.event.ClientJoinWorldCallback;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(at = @At("HEAD"), method = "joinWorld")
    private void compactChat$joinWorldHandler(CallbackInfo ci) {
        ClientJoinWorldCallback.EVENT.invoker().onJoin();
    }
}
