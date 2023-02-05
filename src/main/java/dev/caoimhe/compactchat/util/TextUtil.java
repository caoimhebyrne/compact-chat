package dev.caoimhe.compactchat.util;

import net.minecraft.text.Text;

import java.util.function.Predicate;

public class TextUtil {
    public static Text removeSiblings(Text parent, Predicate<Text> predicate) {
        var copy = parent.copy();
        copy.getSiblings().removeIf(predicate);

        return copy;
    }
}
