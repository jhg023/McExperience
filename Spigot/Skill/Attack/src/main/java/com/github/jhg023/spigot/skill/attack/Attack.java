package com.github.jhg023.spigot.skill.attack;

import com.github.jhg023.spigot.skill.attack.event.AttackEventHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the Attack Skill plugin.
 *
 * @author Jacob Glickman
 * @version January 10, 2020
 */
public final class Attack extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new AttackEventHandler(), this);
    }
}
