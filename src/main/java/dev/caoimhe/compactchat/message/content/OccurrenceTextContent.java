package dev.caoimhe.compactchat.message.content;

import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.Optional;

public class OccurrenceTextContent implements PlainTextContent {
    private final int occurrences;

    public OccurrenceTextContent(final int occurrences) {
        this.occurrences = occurrences;
    }

    public static MutableText create(final int occurrences) {
        return MutableText.of(new OccurrenceTextContent(occurrences));
    }

    @Override
    public String string() {
        return " (" + this.occurrences + ")";
    }

    @Override
    public <T> Optional<T> visit(StringVisitable.StyledVisitor<T> visitor, Style style) {
        return visitor.accept(style, this.string());
    }

    @Override
    public <T> Optional<T> visit(StringVisitable.Visitor<T> visitor) {
        return visitor.accept(this.string());
    }

    @Override
    public String toString() {
        return "compactChatTextOccurrences{occurrences = " + this.occurrences + "}";
    }
}
