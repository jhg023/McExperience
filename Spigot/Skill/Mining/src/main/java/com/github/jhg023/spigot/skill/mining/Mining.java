package com.github.jhg023.spigot.skill.mining;

import com.github.jhg023.spigot.skill.mining.event.MiningEventHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the Mining Skill plugin.
 *
 * @author Jacob Glickman
 * @version January 6, 2020
 */
public final class Mining extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MiningEventHandler(), this);
    }
}
