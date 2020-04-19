package com.github.jhg023.spigot.skill.archery.event;

import com.github.jhg023.spigot.skill.Skill;
import com.github.jhg023.spigot.skill.SkillManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * The class that handles all Archery-related events.
 *
 * @author Jacob Glickman
 * @version January 11, 2020
 */
public final class ArcheryEventHandler implements Listener {

    /**
     * The amount of archery skill experience to provide a {@link Player player} with for each point of damage done on
     * an {@link Entity entity}.
     */
    private static final int ARCHERY_EXPERIENCE_MULTIPLIER = 3;

    @EventHandler
    public void onPlayerDamageEntityWithBow(EntityDamageByEntityEvent event) {
        // If the cause of damage is not a projectile, return.
        if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            return;
        }

        var projectile = (Projectile) event.getDamager();

        // If the projectile is not an arrow, return.
        if (projectile.getType() != EntityType.ARROW) {
            return;
        }

        var shooter = projectile.getShooter();

        // If the shooter isn't a player, return.
        if (!(shooter instanceof Player)) {
            return;
        }

        SkillManager.getInstance().addExperience((Player) shooter, Skill.ARCHERY,
            (int) (ARCHERY_EXPERIENCE_MULTIPLIER * event.getFinalDamage()));
    }
}
