package com.gmail.nossr50.runnables;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.gmail.nossr50.database.Database;
import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.skills.SkillType;
import com.gmail.nossr50.skills.SkillTools;

public class McRankAsync implements Runnable {
    private final String playerName;
    private final CommandSender sender;

    public McRankAsync(String playerName, CommandSender sender) {
        this.playerName = playerName;
        this.sender = sender;
    }

    @Override
    public void run() {
        final Map<String, Integer> skills = Database.readSQLRank(playerName);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("mcMMO"), new Runnable() {
            @Override
            public void run() {
                sender.sendMessage(LocaleLoader.getString("Commands.mcrank.Heading"));
                sender.sendMessage(LocaleLoader.getString("Commands.mcrank.Player", new Object[] {playerName}));

                for (SkillType skillType : SkillType.values()) {
                    if (skillType.isChildSkill()) {
                        continue;
                    }

                    if (skillType.equals(SkillType.ALL))
                        continue; // We want the overall ranking to be at the bottom

                    if (skills.get(skillType.name()) == null) {
                        sender.sendMessage(LocaleLoader.getString("Commands.mcrank.Skill", new Object[] {SkillTools.localizeSkillName(skillType), LocaleLoader.getString("Commands.mcrank.Unranked")} ));
                    }
                    else {
                        sender.sendMessage(LocaleLoader.getString("Commands.mcrank.Skill", new Object[] {SkillTools.localizeSkillName(skillType), skills.get(skillType.name())} ));
                    }
                }

                if (skills.get("ALL") == null) {
                    sender.sendMessage(LocaleLoader.getString("Commands.mcrank.Overall", new Object[] {LocaleLoader.getString("Commands.mcrank.Unranked")} ));
                }
                else {
                    sender.sendMessage(LocaleLoader.getString("Commands.mcrank.Overall", new Object[] {skills.get("ALL")} ));
                }
            }

        }, 1L);
    }
}
