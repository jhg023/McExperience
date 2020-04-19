package com.github.jhg023.spigot.skill.archery;

import com.github.jhg023.spigot.skill.archery.event.ArcheryEventHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the Archery Skill plugin.
 *
 * @author Jacob Glickman
 * @version January 11, 2020
 */
public final class Archery extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ArcheryEventHandler(), this);
    }
}
