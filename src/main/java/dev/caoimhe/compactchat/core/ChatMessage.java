package dev.caoimhe.compactchat.core;

import dev.caoimhe.compactchat.util.TextUtil;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

/**
 * A chat message received by the client.
 * The Text isn't stored here for memory reasons, that's stored in the HashMap linking the message to this.
 */
public class ChatMessage {
    /**
     * A regex pattern to match " (X)", X being any number.
     * It also ensures that the closing parentheses is the end of the string.
     */
    private static final Pattern OCCURRENCES_TEXT_PATTERN = Pattern.compile("\\([0-9]+\\)$");

    /**
     * The styling for the occurrences text
     */
    private static final Style OCCURENCES_TEXT_STYLE = Style.EMPTY.withColor(Formatting.GRAY);

    /**
     * The amount of times this message has occurred.
     */
    private int occurrences = 0;

    /**
     * Increments the occurrences counter.
     */
    public void addOccurrence() {
        occurrences++;
    }

    /**
     * Returns modifiedText appended with the amount of occurences.
     */
    public Text modifiedText(Text unmodifiedText) {
        if (occurrences == 0) {
            return unmodifiedText;
        }

        var occurrencesText = Text
            .literal(" (" + occurrences + ")")
            .setStyle(OCCURENCES_TEXT_STYLE);

        return unmodifiedText.copy().append(occurrencesText);
    }

    /**
     * Returns an unmodified version of a modified text (one that has occurrences appended).
     */
    public Text removeOccurencesText(Text modifiedText) {
        return TextUtil.removeSiblings(modifiedText, this::hasOccurrencesAppended);
    }

    /**
     * If a text is modified with occurences or not
     */
    private boolean hasOccurrencesAppended(Text text) {
        var hasOccurrencesAtTheEnd = OCCURRENCES_TEXT_PATTERN.matcher(text.getString()).find();
        var hasOccurrencesStyle = text.getStyle() == OCCURENCES_TEXT_STYLE;

        return hasOccurrencesAtTheEnd && hasOccurrencesStyle;
    }
}
