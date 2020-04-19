package com.github.jhg023.spigot.skill.utility;

import com.github.jhg023.spigot.skill.Skill;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * A tuple that contains a {@link Skill skill} and the amount of experience to provide for it.
 *
 * @author Jacob Glickman
 * @version January 11, 2020
 */
public final class SkillExperience {

    private final Skill skill;

    private final int experience;

    public SkillExperience(Skill skill, int experience) {
        this.skill = skill;
        this.experience = experience;
    }

    public Skill getSkill() {
        return skill;
    }

    public int getExperience() {
        return experience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SkillExperience that = (SkillExperience) o;

        return experience == that.experience && skill == that.skill;
    }

    @Override
    public int hashCode() {
        return Objects.hash(skill, experience);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SkillExperience.class.getSimpleName() + "[", "]")
            .add("skill=" + skill)
            .add("experience=" + experience)
            .toString();
    }
}
