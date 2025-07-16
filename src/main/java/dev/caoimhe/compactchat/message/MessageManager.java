package dev.caoimhe.compactchat.message;

import dev.caoimhe.compactchat.config.Configuration;
import dev.caoimhe.compactchat.ext.IChatHudExt;
import dev.caoimhe.compactchat.message.content.OccurrenceTextContent;
import dev.caoimhe.compactchat.util.TextUtil;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * Responsible for keeping track of how many times a message has been sent in chat and removing duplicates from the
 * chat history.
 *
 * @see dev.caoimhe.compactchat.mixin.ChatHudMixin
 */
public class MessageManager {
    private static final Style OCCURRENCE_TEXT_STYLE = Style.EMPTY.withColor(Formatting.GRAY);

    private final IChatHudExt chatHud;
    private final Map<String, MessageTracker> messages;
    private @Nullable String previousMessage;

    public MessageManager(final IChatHudExt chatHud) {
        this.chatHud = chatHud;
        this.messages = new HashMap<>();
        this.previousMessage = null;
    }

    /**
     * Attempts to compact an incoming message.
     *
     * @return The compacted message.
     */
    public Text compactMessage(final Text text) {
        // We use a string representation of the message to compare it to another text.
        final String message = TextUtil.stripIgnoredComponents(text);

        if (this.shouldIgnore(text, message)) {
            return text;
        }

        final MessageTracker tracker = this.messages.computeIfAbsent(message, (v) -> new MessageTracker());
        tracker.incrementOccurrences();

        // Certain features require us to know the previous message.
        this.previousMessage = message;

        // If the message has only occurred once (i.e. this is the first occurrence of the message), we don't need to
        // do anything, the message can be accepted as is.
        if (tracker.occurrences() <= 1) {
            return text;
        }

        // In order to append the occurrence counter (and do equality checks), we must make a mutable copy of the message.
        final MutableText mutableMessage = text.copy();

        // Before returning the message with updated occurrences, we should remove any existing messages from the chat
        // history.
        final ListIterator<ChatHudLine> iterator = this.chatHud.compactChat$getMessages().listIterator();
        while (iterator.hasNext()) {
            final ChatHudLine chatHudLine = iterator.next();

            // In order to check equality with the incoming message, we need to remove the occurrence text content.
            final MutableText contentWithoutOccurrences = chatHudLine.content().copy();
            contentWithoutOccurrences.getSiblings().removeIf(it -> it.getContent() instanceof OccurrenceTextContent);

            // In order to do a proper equality check, both instances must be a mutable copy of the message.
            final String content = TextUtil.stripIgnoredComponents(contentWithoutOccurrences);
            if (content.equals(message)) {
                iterator.remove();
                this.chatHud.compactChat$refreshMessages();
                break;
            }
        }

        // We can then create a new Text instance with the OccurrenceTextContent as a child.
        final MutableText occurrencesText = OccurrenceTextContent.create(tracker.occurrences())
            .setStyle(MessageManager.OCCURRENCE_TEXT_STYLE);

        return mutableMessage.append(occurrencesText);
    }

    /**
     * Clears any tracked messages from this {@link MessageManager} instance.
     */
    public void clear() {
        this.messages.clear();
    }

    /**
     * @return Whether the provided message should be ignored for compacting.
     */
    private boolean shouldIgnore(final Text originalText, final String message) {
        if (originalText.getString().isBlank()) {
            return true;
        }

        if (Configuration.instance().onlyCompactConsecutiveMessages) {
            return message.equals(this.previousMessage);
        }

        // Common separators used by servers, e.g. Hypixel.
        if (Configuration.instance().ignoreCommonSeparators) {
            return message.contains("------") || message.contains("======");
        }

        return false;
    }
}
