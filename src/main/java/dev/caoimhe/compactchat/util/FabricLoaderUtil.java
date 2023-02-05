package dev.caoimhe.compactchat.util;

import net.fabricmc.loader.api.FabricLoader;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class FabricLoaderUtil {
    private static final List<String> EXCLUDED_RANDOM_IDS = CollectionUtil.makeArrayList(
        "compact-chat",
        "fabricloader",
        "java",
        "minecraft"
    );

    public static String getRandomModName() {
        var mods = FabricLoader.getInstance().getAllMods();
        var names = mods.stream()
            .map(it -> {
                // We don't want to include mods which are children of others, they are usually unknown to the user.
                if (it.getContainingMod().isPresent()) {
                    return null;
                }

                var metadata = it.getMetadata();
                if (EXCLUDED_RANDOM_IDS.contains(metadata.getId().toLowerCase(Locale.ROOT))) {
                    return null;
                }

                return metadata.getName();
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        try {
            return CollectionUtil.randomFrom(names);
        } catch (Exception e) {
            // We may not have any mods that aren't excluded to pick from
            return "";
        }
    }
}
