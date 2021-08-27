package dev.cbyrne.compactchat.mixin;

import dev.cbyrne.compactchat.util.BetterOrderedText;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Unique
    private final Map<String, Integer> compactChat$unmodifiedMessages = new HashMap<>();

    @Shadow
    @Final
    private List<ChatHudLine<Text>> messages;

    @Shadow
    @Final
    private List<ChatHudLine<OrderedText>> visibleMessages;

    @Shadow
    public abstract void addMessage(Text message);

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void modifyMessage(Text message, CallbackInfo ci) {
        var messageString = message.getString();

        var trimmedString = messageString.trim();
        if (trimmedString.isEmpty() || trimmedString.isBlank()) return;

        var occurrences = compactChat$unmodifiedMessages.get(messageString);
        if (occurrences == null) {
            compactChat$unmodifiedMessages.put(messageString, 1);
            return;
        }

        // Remove existing message(s) from chat
        for (ChatHudLine<Text> chatHudLine : new ArrayList<>(messages)) {
            var text = chatHudLine.getText();
            if (text.getString().equals(messageString) || text.equals(compactChat$addOccurrencesToText(message, occurrences))) {
                compactChat$removeMessageByText(text);
            }
        }

        occurrences++;
        compactChat$unmodifiedMessages.put(messageString, occurrences);

        addMessage(compactChat$addOccurrencesToText(message, occurrences));
        ci.cancel();
    }

    /**
     * Removes a message from chat if it matches the supplied {@link Text}
     *
     * @param text the text to remove from chat
     */
    @Unique
    private void compactChat$removeMessageByText(Text text) {
        visibleMessages.removeIf((message) -> (new BetterOrderedText(message.getText())).getString().equals(text.getString()));
        messages.removeIf((message) -> message.getText().getString().equals(text.getString()));
    }

    @Unique
    private Text compactChat$addOccurrencesToText(Text text, int occurrences) {
        return text.shallowCopy().append(Text.of(" (" + occurrences + ")").shallowCopy().setStyle(Style.EMPTY.withStrikethrough(false).withColor(Formatting.GRAY)));
    }
}
