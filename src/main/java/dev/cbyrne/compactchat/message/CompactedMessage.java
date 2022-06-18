package dev.cbyrne.compactchat.message;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CompactedMessage {
    private static final Style OCCURRENCES_STYLE = Style.EMPTY
        .withStrikethrough(false)
        .withColor(Formatting.GRAY)
        .withFont(Style.DEFAULT_FONT_ID);

    private final Text originalText;
    private int occurrences;

    public CompactedMessage(Text text) {
        this.originalText = text;
        this.occurrences = 1;
    }

    public void incrementOccurrences() {
        occurrences++;
    }

    public Text getOriginalText() {
        return originalText;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public Text getModifiedText() {
        var appendSpace = originalText.getString().endsWith(" ") ? "" : " ";
        var occurrencesText = Text.of(appendSpace + "(" + this.getOccurrences() + ")")
            .copyContentOnly()
            .setStyle(OCCURRENCES_STYLE);

        // Command results can be weird, and don't return the content on `copyContentOnly`.
        // If `copyContentOnly` is empty, let's try `copy`!
        var content = originalText.copyContentOnly();
        if (content.getString().isEmpty()) {
            content = originalText.copy();
        }

        return content.append(occurrencesText);
    }

    public boolean equals(Text text) {
        return this.getOriginalText().getString().equals(text.getString());
    }

    public boolean equals(CompactedMessage message) {
        return this.getOriginalText().getString().equals(message.getOriginalText().getString());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj instanceof CompactedMessage message) {
            // We are comparing to an existing compacted message
            return this.equals(message);
        } else if (obj instanceof Text text) {
            // We are comparing to a text object where we are unsure if it has been compacted or not yet
            return this.equals(text);
        }

        return false;
    }
}