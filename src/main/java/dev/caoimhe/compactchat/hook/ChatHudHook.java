package dev.caoimhe.compactchat.hook;

import dev.caoimhe.compactchat.CompactChatClient;
import dev.caoimhe.compactchat.core.ChatMessage;
import dev.caoimhe.compactchat.hook.extensions.IChatHudExt;
import net.minecraft.text.Text;

import java.util.HashMap;

public class ChatHudHook {
    private final IChatHudExt chatHud;
    /**
     * A historical map of all chat messages sent, mapped to their wrapper class
     */
    private final HashMap<Text, ChatMessage> chatMessages = new HashMap<>();
    /**
     * The previous message received by the client
     */
    private Text previousMessage = null;

    public ChatHudHook(IChatHudExt chatHud) {
        this.chatHud = chatHud;

        // Clear chat history when `onlyCompactConsecutiveMessages` has been toggled.
        CompactChatClient
            .configuration()
            .subscribeToOnlyCompactConsecutiveMessages(value -> chatHud.compactchat$clear());
    }

    /**
     * Returns the modified (if applicable) chat message.
     */
    public Text compactChatMessage(Text message) {
        var chatMessage = this.chatMessages.get(message);
        var previousMessage = this.previousMessage;
        this.previousMessage = message;

        var shouldIgnoreNonConsecutiveMessage = CompactChatClient.configuration().onlyCompactConsecutiveMessages() && !message.equals(previousMessage);
        if (chatMessage == null || shouldIgnoreNonConsecutiveMessage) {
            this.chatMessages.put(message, new ChatMessage());
            return message;
        }

        // This chat message has occurred before, let's remove the old occurrence(s).
        this.removeMessage(message, chatMessage);
        chatMessage.addOccurrence();

        return chatMessage.modifiedText(message);
    }

    /**
     * Removes a message (and its occurrences modifications) from the Chat HUD.
     */
    public void removeMessage(Text originalMessage, ChatMessage message) {
        var iterator = this.chatHud.compactchat$getMessages().listIterator();
        while (iterator.hasNext()) {
            var chatHudLine = iterator.next();

            // We remove occurrences because we want to remove existing compacted messages too.
            var contentWithoutOccurrences = message.removeOccurencesText(chatHudLine.content());
            var textWithoutOccurrences = message.removeOccurencesText(originalMessage);

            if (contentWithoutOccurrences.equals(textWithoutOccurrences)) {
                iterator.remove();
                this.chatHud.compactchat$refreshMessages();

                return;
            }
        }
    }

    /**
     * Called when the Chat HUD is clearing its messages.
     * Since all chat messages aren't visible anymore, we should clear their {@link ChatMessage} too.
     */
    public void onClear() {
        chatMessages.clear();
    }
}
