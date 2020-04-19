package com.github.jhg023.spigot.skill.cooking.event;

import com.github.jhg023.spigot.skill.Skill;
import com.github.jhg023.spigot.skill.SkillManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

/**
 * The class that handles all Cooking-related events.
 *
 * @author Jacob Glickman
 * @version January 11, 2020
 */
public final class CookingEventHandler implements Listener {

    /**
     * The amount of cooking skill experience to provide a {@link Player player} with when they remove a cooked,
     * edible item from a furnace.
     */
    private static final int COOKING_EXPERIENCE_MULTIPLIER = 3;

    @EventHandler
    public void onPlayerRemoveCookedFoodFromFurnace(FurnaceExtractEvent event) {
        // If the item isn't edible, return.
        if (!event.getItemType().isEdible()) {
            return;
        }

        SkillManager.getInstance().createExperienceOrb(event.getPlayer(), event.getBlock().getLocation(),
            Skill.COOKING, COOKING_EXPERIENCE_MULTIPLIER * event.getExpToDrop());
    }
}
