package dev.caoimhe.compactchat.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.caoimhe.compactchat.CompactChatClient;
import javafx.util.Pair;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ChatMessages.class)
public class ChatMessagesMixin {
    @Inject(method = "breakRenderedChatMessageLines", at = @At("HEAD"), cancellable = true)
    private static void compactChat$returnMessageIfCached(
        StringVisitable message,
        int width,
        TextRenderer textRenderer,
        CallbackInfoReturnable<List<OrderedText>> cir
    ) {
        var entry = CompactChatClient.CACHED_SPLIT_MESSAGES.get(new Pair<>(message, width));
        if (entry != null) {
            cir.setReturnValue(entry);
        }
    }

    @ModifyReturnValue(method = "breakRenderedChatMessageLines", at = @At("RETURN"))
    private static List<OrderedText> compactChat$cacheMessage(
        List<OrderedText> original,
        @Local(argsOnly = true) StringVisitable message,
        @Local(argsOnly = true) int width
    ) {
        CompactChatClient.CACHED_SPLIT_MESSAGES.put(new Pair<>(message, width), original);
        return original;
    }
}
