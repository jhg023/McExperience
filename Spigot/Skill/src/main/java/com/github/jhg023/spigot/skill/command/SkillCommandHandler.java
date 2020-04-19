package com.github.jhg023.spigot.skill.command;

import com.github.jhg023.spigot.skill.Skill;
import com.github.jhg023.spigot.skill.SkillManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * The class that handles command for the SkillManager module.
 *
 * @author Jacob Glickman
 * @version January 5, 2020
 */
public final class SkillCommandHandler implements TabExecutor {

    /**
     * The sorted list of {@link Skill skill} names to display in the auto-complete box when a {@link Player player}
     * is typing a command.
     */
    private static final List<String> TAB_COMPLETE_SKILL_LIST = Arrays.stream(Skill.values())
        .map(Enum::name).map(String::toLowerCase).sorted().collect(Collectors.toUnmodifiableList());

    /**
     * An instance of the {@link SkillManager} class.
     */
    private final SkillManager manager;

    public SkillCommandHandler(SkillManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (label.toLowerCase(Locale.US)) {
            case "track":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only in-game players can use the /track command!");
                    return true;
                }

                if (args.length != 1) {
                    sender.sendMessage("Usage: /track [skill name]");
                    return true;
                }

                Skill skill;

                try {
                    skill = Skill.valueOf(args[0].toUpperCase(Locale.US));
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("You have specified an invalid skill name!");
                    return true;
                }

                manager.updateTracker((Player) sender, skill);
                return true;
            default:
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (alias) {
            case "track":
                if (args.length == 0) {
                    return TAB_COMPLETE_SKILL_LIST;
                }

                return TAB_COMPLETE_SKILL_LIST.stream()
                    .filter(skill -> skill.startsWith(args[0].toLowerCase(Locale.US)))
                    .collect(Collectors.toUnmodifiableList());
            default:
                return List.of();
        }
    }
}
