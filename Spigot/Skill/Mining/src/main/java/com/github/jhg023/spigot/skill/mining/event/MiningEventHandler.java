package com.github.jhg023.spigot.skill.mining.event;

import com.github.jhg023.spigot.skill.Skill;
import com.github.jhg023.spigot.skill.SkillManager;
import com.github.jhg023.spigot.skill.mining.Mineable;
import com.github.jhg023.spigot.skill.mining.Pickaxe;
import com.github.jhg023.spigot.skill.utility.SkillUtility;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;

/**
 * The class that handles all Mining-related events.
 *
 * @author Jacob Glickman
 * @version January 6, 2020
 */
public final class MiningEventHandler implements Listener {

    @EventHandler
    public void onPlayerDamageMineableBlock(BlockDamageEvent event) {
        var material = event.getBlock().getType();
        var maybeMinable = Mineable.forMaterial(material);

        if (maybeMinable.isEmpty()) {
            return;
        }

        var mineable = maybeMinable.get();

        if (SkillManager.getInstance().doesNotMeetLevelRequirement(event.getPlayer(), Skill.MINING,
                mineable.getRequiredMiningLevel(), level -> "mine &a" + mineable.getFormattedName() + "&f!")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamageBlockWithPickaxe(BlockDamageEvent event) {
        var player = event.getPlayer();
        var maybePickaxe = Pickaxe.forMaterial(player.getInventory().getItemInMainHand().getType());

        if (maybePickaxe.isEmpty()) {
            return;
        }

        var pickaxe = maybePickaxe.get();

        if (SkillManager.getInstance().doesNotMeetLevelRequirement(player, Skill.MINING,
                pickaxe.getRequiredMiningLevel(), (int level) -> {
                    String formattedName = pickaxe.getFormattedName();
                    return "use " + SkillUtility.getIndefiniteArticle(formattedName) + " &a" + formattedName + "&f!";
                })) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreakMineableBlockWithPickaxe(BlockBreakEvent event) {
        var player = event.getPlayer();
        var pickaxeItem = player.getInventory().getItemInMainHand();
        var maybePickaxe = Pickaxe.forMaterial(pickaxeItem.getType());

        // If the player isn't wielding a pickaxe, then we should not give them experience.
        if (maybePickaxe.isEmpty()) {
            return;
        }

        var pickaxe = maybePickaxe.get();

        // The player could have started mining this block with something other than a pickaxe, and switched to a
        // pickaxe that they don't have the required mining level to use. If that is the case, we need to verify that
        // they have the required mining level to use this pickaxe and, if not, cancel the event.
        if (SkillManager.getInstance().doesNotMeetLevelRequirement(player, Skill.MINING,
                pickaxe.getRequiredMiningLevel(), (int level) -> {
                    String formattedName = pickaxe.getFormattedName();
                    return "use " + SkillUtility.getIndefiniteArticle(formattedName) + " &a" + formattedName + "&f!";
                })) {
            event.setCancelled(true);
            return;
        }

        // If the player's pickaxe is enchanted with Silk Touch, then we should not give them experience.
        if (pickaxeItem.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
            return;
        }

        var maybeMineable = Mineable.forMaterial(event.getBlock().getType());

        // The player mined a block with a pickaxe, but the block was not one we give mining experience for.
        if (maybeMineable.isEmpty()) {
            return;
        }

        var mineable = maybeMineable.get();

        if (SkillManager.getInstance().doesNotMeetLevelRequirement(player, Skill.MINING,
                mineable.getRequiredMiningLevel(), level -> "mine &a" + mineable.getFormattedName() + "&f!")) {
            event.setCancelled(true);
            return;
        }

        SkillManager.getInstance().createExperienceOrb(player, event.getBlock().getLocation(), Skill.MINING,
            mineable.getMiningExperience());
    }
}
