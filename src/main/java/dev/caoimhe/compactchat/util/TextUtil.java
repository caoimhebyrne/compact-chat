package dev.caoimhe.compactchat.util;

import dev.caoimhe.compactchat.config.Configuration;
import net.minecraft.text.Text;

public class TextUtil {
    private TextUtil() {
    }

    /**
     * Converts a {@link Text} instance into a {@link String} that can be used to compare it with other {@link Text}.
     */
    public static String stripIgnoredComponents(final Text text) {
        final String textString = text.getString();

        if (Configuration.instance().ignoreFirstCharactersCount > 0) {
            final int start = Math.min(textString.length(), Configuration.instance().ignoreFirstCharactersCount);
            return textString.substring(start);
        }

        return textString;
    }
}
