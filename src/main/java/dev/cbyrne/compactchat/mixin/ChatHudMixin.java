package dev.cbyrne.compactchat.mixin;

import dev.cbyrne.compactchat.CompactChat;
import dev.cbyrne.compactchat.config.Configuration;
import dev.cbyrne.compactchat.message.CompactedMessage;
import dev.cbyrne.compactchat.util.BetterOrderedText;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
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

        // Check if this message has been compacted before
        var optionalCompactedMessage = CompactChat.COMPACTED_MESSAGES
            .stream()
            .filter(it -> it.equals(message))
            .findFirst();

        // If this message has not been compacted before, let's add it to the list.
        if (optionalCompactedMessage.isEmpty()) {
            CompactChat.COMPACTED_MESSAGES.add(new CompactedMessage(message));
            return;
        }

        // Return if the previous message was not the same as this one
        if (Configuration.getInstance().onlyCompactConsecutiveMessages && !messages.isEmpty()) {
            var previousMessage = messages.get(0).getText();
            if (!previousMessage.getString().equals(messageString)) return;
        }

        // Remove existing message(s) from chat
        for (ChatHudLine<Text> chatHudLine : new ArrayList<>(messages)) {
            var text = chatHudLine.getText();
            if (text.getString().equals(messageString)) {
                compactChat$removeMessageByText(text);
            }
        }

        // Remove the occurrences of the last counter
        var compactedMessage = optionalCompactedMessage.get();
        compactChat$removeMessageByText(compactedMessage.getModifiedText());

        // Increment the counter and send the new message
        compactedMessage.incrementOccurrences();
        addMessage(compactedMessage.getModifiedText());

        ci.cancel();
    }

    /**
     * Modify the chat history length to be infinite
     *
     * @author asbyth
     */
    @ModifyConstant(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", constant = @Constant(intValue = 100))
    private int modifyChatHistoryLength(int value) {
        return Configuration.getInstance().infiniteChatHistory ? Integer.MAX_VALUE : value;
    }

    /**
     * Removes a message from chat if it matches the supplied {@link Text}
     *
     * @param text the text to remove from chat
     */
    @Unique
    private void compactChat$removeMessageByText(Text text) {
        visibleMessages.removeIf((message) -> BetterOrderedText.getString(message.getText()).equals(text.getString()));
        messages.removeIf((message) -> message.getText().getString().equals(text.getString()));
    }
}
