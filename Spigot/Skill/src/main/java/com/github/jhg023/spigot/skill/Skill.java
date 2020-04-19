package com.github.jhg023.spigot.skill;

import org.bukkit.ChatColor;

import java.util.Locale;

/**
 * An enumeration of skills belonging to McExperience.
 * <br><br>
 * If any values are added to this enumeration, they <strong>must first be added to phpMyAdmin</strong>!
 *
 * @author Jacob Glickman
 * @version January 5, 2020
 */
public enum Skill {

    ATTACK("Attack"),
    ARCHERY("Archery"),
    COOKING("Cooking"),
    CRAFTING("Crafting"),
    FARMING("Farming"),
    FISHING("Fishing"),
    MINING("Mining"),
    WOODCUTTING("Woodcutting");

    private final String display;

    private final String formattedDisplay;

    private final String maxLevelFormattedDisplay;

    private final String databaseColumnName;

    Skill(String display) {
        this.display = display;
        this.formattedDisplay = ChatColor.translateAlternateColorCodes('&',
            "&f" + display + " - Level &a%d &f/ &a%d &f- Experience &a%s &f/ &a%s");
        this.maxLevelFormattedDisplay = ChatColor.translateAlternateColorCodes('&',
            "&f" + display + " - Level &a%d &f- Experience &a%s");
        this.databaseColumnName = name().toLowerCase(Locale.US) + "_skill_exp";
    }

    public String getDisplay() {
        return display;
    }

    public String getFormattedDisplay() {
        return formattedDisplay;
    }

    public String getMaxLevelFormattedDisplay() {
        return maxLevelFormattedDisplay;
    }

    public String getDatabaseColumnName() {
        return databaseColumnName;
    }
}
