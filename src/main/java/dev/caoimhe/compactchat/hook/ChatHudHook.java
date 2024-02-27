package dev.caoimhe.compactchat.hook;

import dev.caoimhe.compactchat.CompactChatClient;
import dev.caoimhe.compactchat.config.Configuration;
import dev.caoimhe.compactchat.core.ChatMessage;
import dev.caoimhe.compactchat.hook.extensions.IChatHudExt;
import dev.caoimhe.compactchat.util.TextUtil;
import net.minecraft.text.Text;

import java.util.HashMap;

public class ChatHudHook {
    private final IChatHudExt chatHud;

    /**
     * A historical map of all chat messages sent, mapped to their wrapper class
     */
    private final HashMap<String, ChatMessage> chatMessages = new HashMap<>();

    /**
     * The previous message received by the client
     */
    private Text previousMessage = null;

    public ChatHudHook(IChatHudExt chatHud) {
        this.chatHud = chatHud;

        // Clear chat history when the configuration has been saved.
        Configuration.getInstance().onSave(chatHud::compactchat$clear);
    }

    /**
     * Returns the modified (if applicable) chat message.
     */
    public Text compactChatMessage(Text message) {
        // We only use the message without timestamps when comparing to the previous/other messages.
        // This is because the timestamps are not part of the message, and thus should not be used for comparison.
        // However, we do want to keep the timestamps in the message, so we can't remove them from the message.
        // See GitHub issue #23 for more information.
        var withoutTimestamps = TextUtil.removeTimestamps(message);

        var chatMessage = this.chatMessages.get(message.getString());
        var previousMessage = this.previousMessage;
        this.previousMessage = withoutTimestamps;

        var shouldIgnoreNonConsecutiveMessage = Configuration.getInstance().onlyCompactConsecutiveMessages && !withoutTimestamps.equals(previousMessage);
        if (chatMessage == null || shouldIgnoreNonConsecutiveMessage || shouldIgnoreCommonSeparator(message)) {
            this.chatMessages.put(withoutTimestamps.getString(), new ChatMessage());
            return message;
        }

        // This chat message has occurred before, let's remove the old occurrence(s) - only if
        // it's under the maximum occurrence count.
        if (chatMessage.occurrences < Configuration.getInstance().maximumOccurrences) {
            chatMessage.addOccurrence();
        }

        this.removeMessage(message, chatMessage);
        return chatMessage.modifiedText(message);
    }

    /**
     * If the option is enabled, common separators will be ignored.
     * A message is a common separator if it contains ====== or -------
     */
    private boolean shouldIgnoreCommonSeparator(Text message) {
        if (!Configuration.getInstance().ignoreCommonSeparators) {
            return false;
        }

        var trimmedString = message.getString().trim();
        return trimmedString.isEmpty()
            || trimmedString.isBlank()
            || trimmedString.contains("------")
            || trimmedString.contains("======");
    }

    /**
     * Removes a message (and its occurrences modifications) from the Chat HUD.
     */
    public void removeMessage(Text originalMessage, ChatMessage message) {
        var iterator = this.chatHud.compactchat$getMessages().listIterator();
        while (iterator.hasNext()) {
            var chatHudLine = iterator.next();

            // We remove occurrences because we want to remove existing compacted messages too.
            var contentWithoutOccurrences = message.removeTextModifications(chatHudLine.content());
            var textWithoutOccurrences = message.removeTextModifications(originalMessage);

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
    public void reset() {
        chatMessages.clear();
        CompactChatClient.SPLIT_MESSAGES_CACHE.invalidateAll();
    }
}
