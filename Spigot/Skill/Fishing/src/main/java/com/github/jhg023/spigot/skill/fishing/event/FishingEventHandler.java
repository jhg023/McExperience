package com.github.jhg023.spigot.skill.fishing.event;

import com.github.jhg023.spigot.skill.Skill;
import com.github.jhg023.spigot.skill.SkillManager;
import com.github.jhg023.spigot.skill.fishing.Fishable;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * The class that handles all Fishing-related event.
 *
 * @author Jacob G.
 * @version January 7, 2019
 */
public final class FishingEventHandler implements Listener {

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        // We only want to give the player experience if they caught a fish/item.
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
            return;
        }

        var item = ((Item) event.getCaught()).getItemStack();
        var maybeFishable = Fishable.forMaterial(item.getType());

        if (maybeFishable.isEmpty()) {
            return;
        }

        var player = event.getPlayer();

        SkillManager.getInstance().createExperienceOrb(player, player.getLocation(), Skill.FISHING,
            maybeFishable.get().getFishingExperience() * item.getAmount());
    }
}
