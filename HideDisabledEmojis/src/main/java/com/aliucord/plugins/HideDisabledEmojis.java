// based on https://gitdab.com/distok/cutthecord/src/branch/master/patches/hideunusableemojis/1371.patch

package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.CollectionUtils;
import com.aliucord.entities.Plugin;
import com.discord.models.domain.emoji.Emoji;

import java.util.*;

@SuppressWarnings({"unchecked", "unused"})
public class HideDisabledEmojis extends Plugin {
    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[]{ new Manifest.Author("Juby210", 324622488644616195L) };
        manifest.description = "Hides disabled emojis in emoji picker and autocomplete.";
        manifest.version = "1.0.1";
        manifest.updateUrl = "https://raw.githubusercontent.com/Juby210/Aliucord-plugins/builds/updater.json";
        return manifest;
    }

    private static final String className = "com.discord.widgets.chat.input.emoji.EmojiPickerViewModel$Companion";
    public static Map<String, List<String>> getClassesToPatch() {
        Map<String, List<String>> map = new HashMap<>();
        map.put(className, Collections.singletonList("buildEmojiListItems"));
        return map;
    }

    @Override
    public void start(Context context) {
        patcher.prePatch(className, "buildEmojiListItems", (_this, args) -> {
            Collection<? extends Emoji> emojis = (Collection<? extends Emoji>) args.get(0);
            if (!(emojis instanceof ArrayList)) emojis = new ArrayList<>(emojis);
            CollectionUtils.removeIf(emojis, e -> !e.isUsable());
            args.set(0, emojis);
            return null;
        });
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }
}
