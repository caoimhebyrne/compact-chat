package dev.cbyrne.compactchat.util;

import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

/**
 * This would not be needed if Mojang added {@link Text#getString()} to {@link OrderedText}, but... they probably won't.
 */
public class BetterOrderedText {
    public static String getString(OrderedText text) {
        var visitor = new OrderedTextStringVisitor();
        text.accept(visitor);

        return visitor.getString();
    }

    private static class OrderedTextStringVisitor implements CharacterVisitor {
        private final StringBuilder builder = new StringBuilder();

        @Override
        public boolean accept(int index, Style style, int codePoint) {
            builder.appendCodePoint(codePoint);
            return true;
        }

        public String getString() {
            return builder.toString();
        }
    }
}
