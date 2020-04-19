package com.github.jhg023.spigot.skill.woodcutting.event;

import com.github.jhg023.spigot.skill.woodcutting.Axe;
import com.github.jhg023.spigot.skill.Skill;
import com.github.jhg023.spigot.skill.SkillManager;
import com.github.jhg023.spigot.skill.utility.SkillUtility;
import com.github.jhg023.spigot.skill.woodcutting.Log;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;

/**
 * The class that handles all Woodcutting-related events.
 *
 * @author Jacob Glickman
 * @version January 9, 2020
 */
public final class WoodcuttingEventHandler implements Listener {

    @EventHandler
    public void onPlayerDamageLog(BlockDamageEvent event) {
        var maybeLog = Log.forMaterial(event.getBlock().getType());

        if (maybeLog.isEmpty()) {
            return;
        }

        var log = maybeLog.get();

        if (SkillManager.getInstance().doesNotMeetLevelRequirement(event.getPlayer(), Skill.WOODCUTTING,
                log.getRequiredWoodcuttingLevel(), (int level) -> {
                    String formattedName = log.getFormattedName();
                    return "chop " + SkillUtility.getIndefiniteArticle(formattedName) + " &a" + formattedName + "&f!";
                })) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamageBlockWithAxe(BlockDamageEvent event) {
        var player = event.getPlayer();
        var maybeAxe = Axe.forMaterial(player.getInventory().getItemInMainHand().getType());

        if (maybeAxe.isEmpty()) {
            return;
        }

        var axe = maybeAxe.get();

        if (SkillManager.getInstance().doesNotMeetLevelRequirement(player, Skill.WOODCUTTING,
                axe.getRequiredWoodcuttingLevel(), (int level) -> {
                    String formattedName = axe.getFormattedName();
                    return "use " + SkillUtility.getIndefiniteArticle(formattedName) + " &a" + formattedName + "&f!";
                })) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreakLogWithAxe(BlockBreakEvent event) {
        var player = event.getPlayer();
        var axeItem = player.getInventory().getItemInMainHand();
        var maybeAxe = Axe.forMaterial(axeItem.getType());

        // If the player isn't wielding an axe, then we should not give them experience.
        if (maybeAxe.isEmpty()) {
            return;
        }

        var axe = maybeAxe.get();

        // The player could have started chopping this block with something other than an axe, and switched to an
        // axe that they don't have the required woodcutting level to use. If that is the case, we need to verify that
        // they have the required woodcutting level to use this axe and, if not, cancel the event.
        if (SkillManager.getInstance().doesNotMeetLevelRequirement(player, Skill.WOODCUTTING,
                axe.getRequiredWoodcuttingLevel(), (int level) -> {
                    String formattedName = axe.getFormattedName();
                    return "use " + SkillUtility.getIndefiniteArticle(formattedName) + " &a" + formattedName + "&f!";
                })) {
            event.setCancelled(true);
            return;
        }

        var maybeLog = Log.forMaterial(event.getBlock().getType());

        // The player chopped a log with an axe, but the block was not one we give woodcutting experience for.
        if (maybeLog.isEmpty()) {
            return;
        }

        var log = maybeLog.get();

        if (SkillManager.getInstance().doesNotMeetLevelRequirement(player, Skill.WOODCUTTING,
                log.getRequiredWoodcuttingLevel(), (int level) -> {
                    String formattedName = log.getFormattedName();
                    return "chop " + SkillUtility.getIndefiniteArticle(formattedName) + " &a" + formattedName + "&f!";
                })) {
            event.setCancelled(true);
            return;
        }

        SkillManager.getInstance().createExperienceOrb(player, event.getBlock().getLocation(), Skill.WOODCUTTING,
            log.getWoodcuttingExperience());
    }
}
