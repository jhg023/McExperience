package com.github.jhg023.spigot.skill.cooking;

import com.github.jhg023.spigot.skill.cooking.event.CookingEventHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the Cooking Skill plugin.
 *
 * @author Jacob Glickman
 * @version January 11, 2020
 */
public final class Cooking extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new CookingEventHandler(), this);
    }
}
