package com.aliucord.plugins.tags;

import com.aliucord.Utils;
import com.aliucord.api.CommandsAPI;
import com.aliucord.api.SettingsAPI;
import com.aliucord.entities.MessageEmbed;
import com.aliucord.plugins.Tags;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DeleteCommand {
    public static CommandsAPI.CommandResult execute(Map<String, ?> args, SettingsAPI sets, Tags main) {
        String name = (String) args.get("name");

        MessageEmbed embed = new MessageEmbed();
        if (name == null || name.equals("")) {
            embed.setTitle("Missing required arguments");
        } else {
            embed.setTitle("Successfully deleted tag");
            embed.setColor(0xFF00FF00);
            HashMap<String, String> tags = sets.getObject("tags", new HashMap<>(), Tags.tagsType);
            tags.remove(name);
            sets.setObject("tags", tags);
            Utils.removeIf(main.existingTags, tag -> tag.a().equals(name));
            Utils.removeIf(main.subcommands, option -> option.getName().equals(name));
        }

        return new CommandsAPI.CommandResult(null, Collections.singletonList(embed), false);
    }
}