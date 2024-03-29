package dev.caoimhe.compactchat.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.caoimhe.compactchat.CompactChatClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import org.apache.commons.lang3.tuple.Pair;
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
        var entry = CompactChatClient.SPLIT_MESSAGES_CACHE.getIfPresent(Pair.of(message.getString(), width));
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
        CompactChatClient.SPLIT_MESSAGES_CACHE.put(Pair.of(message.getString(), width), original);
        return original;
    }
}
