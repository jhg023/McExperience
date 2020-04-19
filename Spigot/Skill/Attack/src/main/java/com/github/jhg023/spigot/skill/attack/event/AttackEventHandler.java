package com.github.jhg023.spigot.skill.attack.event;

import com.github.jhg023.spigot.skill.Skill;
import com.github.jhg023.spigot.skill.SkillManager;
import com.github.jhg023.spigot.skill.attack.Weapon;
import com.github.jhg023.spigot.skill.utility.SkillUtility;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * The class that handles all Attack-related events.
 *
 * @author Jacob Glickman
 * @version January 11, 2020
 */
public final class AttackEventHandler implements Listener {

    /**
     * The amount of attack skill experience to provide a {@link Player player} with for each point of damage done on
     * an {@link Entity entity}.
     */
    private static final int ATTACK_EXPERIENCE_MULTIPLIER = 3;

    @EventHandler
    public void onPlayerDamageEntityWithWeapon(EntityDamageByEntityEvent event) {
        // If the cause of damage is not a direct entity attack, return.
        switch (event.getCause()) {
            case ENTITY_ATTACK:
            case ENTITY_SWEEP_ATTACK:
                break;
            default:
                return;
        }

        var damager = event.getDamager();

        // If the damager isn't a player, return.
        if (!(damager instanceof Player)) {
            return;
        }

        var attacker = (Player) damager;
        var maybeWeapon = Weapon.forMaterial(attacker.getInventory().getItemInMainHand().getType());

        // If the attacker isn't wielding a weapon, return.
        if (maybeWeapon.isEmpty()) {
            return;
        }

        var weapon = maybeWeapon.get();

        if (SkillManager.getInstance().doesNotMeetLevelRequirement(attacker, Skill.ATTACK,
                weapon.getRequiredAttackLevel(), (int level) -> {
                    String formattedName = weapon.getFormattedName();
                    return "attack with " + SkillUtility.getIndefiniteArticle(formattedName) + " &a" + formattedName +
                        "&f!";
                })) {
            event.setCancelled(true);
            return;
        }

        SkillManager.getInstance().addExperience(attacker, Skill.ATTACK,
            (int) (ATTACK_EXPERIENCE_MULTIPLIER * event.getFinalDamage()));
    }
}
