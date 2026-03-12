package dev.caoimhe.compactchat.message;

import dev.caoimhe.compactchat.config.Configuration;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.jetbrains.annotations.Nullable;

/**
 * Tracks data associated with a message sent by a user.
 * <p></p>
 * The user that sent the message does not matter, if the content is the same (or similar), it will have the same
 * {@link MessageTracker} instance.
 *
 * @see MessageManager
 */
public class MessageTracker {
    private int occurrences = 0;
    private @Nullable ChatHudLine line = null;

    /**
     * @return The number of times the message occurred in chat.
     */
    public int occurrences() {
        return this.occurrences;
    }

    /**
     * @return The latest {@link ChatHudLine} for this message, if one exists.
     */
    public @Nullable ChatHudLine line() {
        return this.line;
    }

    /**
     * Increments the number of times that a message occurred in chat.
     */
    public void incrementOccurrences() {
        if (this.occurrences == Configuration.instance().maximumOccurrences) {
            return;
        }

        this.occurrences++;
    }

    /**
     * Updates the tracked {@link ChatHudLine} for this message.
     */
    public void setLine(final @Nullable ChatHudLine line) {
        this.line = line;
    }
}
