package com.github.jhg023.spigot.skill.event;

import com.github.jhg023.spigot.skill.Skill;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The class that represents a Bukkit {@link Event} for when a {@link Player} levels up a {@link Skill}.
 *
 * @author Jacob Glickman
 * @version January 5, 2020
 */
public final class PlayerSkillLevelUpEvent extends Event {

    /**
     * This event's {@link HandlerList}.
     */
    private static final HandlerList HANDLER_LIST = new HandlerList();

    /**
     * The new level of the skill that was leveled up.
     */
    private final int newLevel;

    /**
     * The skill that was leveled up.
     */
    private final Skill skill;

    /**
     * The player that leveled up a skill.
     */
    private final Player player;

    /**
     * Creates a new {@link PlayerSkillLevelUpEvent} with the specified {@link Player}, {@link Skill}, and {@code
     * newLevel}.
     *
     * @param player   the player who leveled up.
     * @param skill    the skill that was leveled up.
     * @param newLevel the new level of the skill that was leveled up.
     */
    public PlayerSkillLevelUpEvent(Player player, Skill skill, int newLevel) {
        this.player = player;
        this.skill = skill;
        this.newLevel = newLevel;
    }

    /**
     * Gets the {@link Player} who leveled up a specified {@link Skill}.
     *
     * @return the player who leveled up.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the {@link Skill} that was leveled up by the specified {@link Player}.
     *
     * @return the skill that was leveled up.
     */
    public Skill getSkill() {
        return skill;
    }

    /**
     * Gets the new level of the {@link Player}'s {@link Skill} that was leveled up.
     *
     * @return the new level of the skill.
     */
    public int getNewLevel() {
        return newLevel;
    }

    /**
     * Gets this event's {@link HandlerList}.
     *
     * @return this event's handler list.
     */
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * Gets this event's {@link HandlerList}, statically.
     *
     * @return this event's handler list.
     */
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
