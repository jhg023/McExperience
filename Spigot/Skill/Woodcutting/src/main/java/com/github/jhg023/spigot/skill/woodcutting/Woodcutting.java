package com.github.jhg023.spigot.skill.woodcutting;

import com.github.jhg023.spigot.skill.woodcutting.event.WoodcuttingEventHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the Woodcutting Skill plugin.
 *
 * @author Jacob Glickman
 * @version January 9, 2020
 */
public final class Woodcutting extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new WoodcuttingEventHandler(), this);
    }
}
