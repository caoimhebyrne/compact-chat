package dev.caoimhe.compactchat.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CollectionUtil {
    public static <E> ArrayList<E> makeArrayList(E... elements) {
        return new ArrayList<E>(Arrays.asList(elements));
    }

    public static <E> E randomFrom(List<E> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
}
