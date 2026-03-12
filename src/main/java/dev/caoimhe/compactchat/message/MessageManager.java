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

import java.util.LinkedHashMap;
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
    private final LinkedHashMap<String, MessageTracker> messages;
    private @Nullable String previousMessage;

    public MessageManager(final IChatHudExt chatHud) {
        this.chatHud = chatHud;
        this.messages = new LinkedHashMap<>() {
            @Override
            protected boolean removeEldestEntry(final Map.Entry<String, MessageTracker> eldest) {
                return this.size() > Configuration.instance().maxTrackedMessages;
            }
        };
        this.previousMessage = null;
    }

    /**
     * Attempts to compact an incoming message.
     *
     * @return The compacted message.
     */
    public Text compactMessage(final Text text) {
        this.trimToConfiguredCap();

        // We use a string representation of the message to compare it to another text.
        final String message = TextUtil.stripIgnoredComponents(text);

        final MessageTracker tracker = this.messages.computeIfAbsent(message, (v) -> new MessageTracker());
        final boolean shouldIgnore = this.shouldIgnore(text, message);
        this.previousMessage = message;

        if (shouldIgnore) {
            if (tracker.occurrences() == 0) tracker.incrementOccurrences();
            return text;
        }

        tracker.incrementOccurrences();

        // If the message has only occurred once (i.e. this is the first occurrence of the message), we don't need to
        // do anything, the message can be accepted as is.
        if (tracker.occurrences() <= 1) {
            return text;
        }

        // In order to append the occurrence counter (and do equality checks), we must make a mutable copy of the message.
        final MutableText mutableMessage = text.copy();

        // Use the tracked line reference to avoid scanning the full chat history for duplicates.
        final ChatHudLine previousLine = tracker.line();
        if (previousLine != null) {
            this.chatHud.compactChat$getMessages().remove(previousLine);
            tracker.setLine(null);
            this.chatHud.compactChat$refreshMessages();
        }

        // We can then create a new Text instance with the OccurrenceTextContent as a child.
        final MutableText occurrencesText = OccurrenceTextContent.create(tracker.occurrences())
            .setStyle(MessageManager.OCCURRENCE_TEXT_STYLE);

        return mutableMessage.append(occurrencesText);
    }

    /**
     * Tracks the latest chat line after it has been inserted into the HUD list.
     */
    public void trackLastAddedMessage() {
        final java.util.List<ChatHudLine> chatLines = this.chatHud.compactChat$getMessages();
        if (chatLines.isEmpty()) {
            return;
        }

        final ChatHudLine latestLine = chatLines.getFirst();
        final MutableText contentWithoutOccurrences = latestLine.content().copy();
        contentWithoutOccurrences.getSiblings().removeIf(it -> it.getContent() instanceof OccurrenceTextContent);

        final String message = TextUtil.stripIgnoredComponents(contentWithoutOccurrences);
        final MessageTracker tracker = this.messages.get(message);
        if (tracker != null) {
            tracker.setLine(latestLine);
        }
    }

    /**
     * Clears any tracked messages from this {@link MessageManager} instance.
     */
    public void clear() {
        this.messages.clear();
    }

    /**
     * Applies the current config cap immediately when it is reduced at runtime.
     */
    private void trimToConfiguredCap() {
        final int maxTrackedMessages = Configuration.instance().maxTrackedMessages;
        while (this.messages.size() > maxTrackedMessages) {
            final String eldestKey = this.messages.keySet().iterator().next();
            this.messages.remove(eldestKey);
        }
    }

    /**
     * @return Whether the provided message should be ignored for compacting.
     */
    private boolean shouldIgnore(final Text originalText, final String message) {
        if (originalText.getString().isBlank()) {
            return true;
        }

        if (Configuration.instance().onlyCompactConsecutiveMessages) {
            return !message.equals(this.previousMessage);
        }

        // Common separators used by servers, e.g. Hypixel.
        if (Configuration.instance().ignoreCommonSeparators) {
            return Configuration.instance().commonSeparators.stream().filter(it -> !it.isBlank())
                .anyMatch(message::contains);
        }

        return false;
    }
}
