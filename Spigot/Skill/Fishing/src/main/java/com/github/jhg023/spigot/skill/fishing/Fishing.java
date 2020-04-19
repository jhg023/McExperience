package com.github.jhg023.spigot.skill.fishing;

import com.github.jhg023.spigot.skill.fishing.event.FishingEventHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the Fishing Skill plugin.
 *
 * @author Jacob Glickman
 * @version January 11, 2020
 */
public final class Fishing extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new FishingEventHandler(), this);
    }
}
