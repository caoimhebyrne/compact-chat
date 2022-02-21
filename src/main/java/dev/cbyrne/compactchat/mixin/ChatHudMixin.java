package dev.cbyrne.compactchat.mixin;

import dev.cbyrne.compactchat.CompactChat;
import dev.cbyrne.compactchat.config.Configuration;
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
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    private final Style compactChat$occurrencesStyle = Style.EMPTY
            .withStrikethrough(false)
            .withColor(Formatting.GRAY)
            .withFont(Style.DEFAULT_FONT_ID);

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
        if (trimmedString.isEmpty() || trimmedString.isBlank() || trimmedString.contains("--------")) return;

        var occurrences = CompactChat.messageCounters.get(messageString);
        if (occurrences == null) {
            CompactChat.messageCounters.put(messageString, 1);
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
        CompactChat.messageCounters.put(messageString, occurrences);

        addMessage(compactChat$addOccurrencesToText(message, occurrences));
        ci.cancel();
    }

    /**
     * Modify the chat history length to be infinite
     *
     * @author asbyth
     */
    @ModifyConstant(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", constant = @Constant(intValue = 100))
    private int modifyChatHistoryLength(int value) {
        return Configuration.INSTANCE.infiniteChatHistory ? Integer.MAX_VALUE : value;
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
        // Some servers will add an extra space at the end of messages.
        // Instead of trimming this as it may ruin the formatting of the message, we're just going to check if it
        // ends with a space, and if it does then don't append another space to separate it from the occurrence counter

        var appendSpace = text.getString().endsWith(" ") ? "" : " ";
        var occurrencesText = Text.of(appendSpace + "(" + occurrences + ")")
                .shallowCopy()
                .setStyle(compactChat$occurrencesStyle);

        return text.shallowCopy().append(occurrencesText);
    }
}
