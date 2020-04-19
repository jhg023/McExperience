package com.github.jhg023.spigot.skill;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * A class that represents a skill being tracked on a {@link Player player}'s screen.
 *
 * @author Jacob Glickman
 * @version January 5, 2020
 */
public final class SkillTracker {

    /**
     * The {@link Skill} being tracked.
     */
    private final Skill skill;

    /**
     * The {@link BossBar} used to keep track of the specified {@link Skill}'s progress.
     */
    private final BossBar bar;

    /**
     * Creates a new {@link SkillTracker} object with the specified {@link Skill} and {@link BossBar}.
     *
     * @param skill the skill to track.
     * @param bar   the bar used to track progress.
     */
    public SkillTracker(Skill skill, BossBar bar) {
        this.skill = skill;
        this.bar = bar;
    }

    /**
     * Gets the {@link Skill} being tracked.
     *
     * @return the skill being tracked.
     */
    public Skill getSkill() {
        return skill;
    }

    /**
     * Gets the {@link BossBar} used to track progress.
     *
     * @return the progress tracker.
     */
    public BossBar getBar() {
        return bar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SkillTracker that = (SkillTracker) o;

        return skill == that.skill && Objects.equals(bar, that.bar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skill, bar);
    }
}
