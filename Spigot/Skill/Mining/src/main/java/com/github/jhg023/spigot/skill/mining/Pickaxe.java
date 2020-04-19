package com.github.jhg023.spigot.skill.mining;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * An enumeration of pickaxes and their respective mining level requirements.
 *
 * @author Jacob Glickman
 * @version January 6, 2020
 */
public enum Pickaxe {

    WOODEN_PICKAXE(1, "Wooden Pickaxe", Material.WOODEN_PICKAXE),
    STONE_PICKAXE(11, "Stone Pickaxe", Material.STONE_PICKAXE),
    IRON_PICKAXE(21, "Iron Pickaxe", Material.IRON_PICKAXE),
    GOLDEN_PICKAXE(31, "Golden Pickaxe", Material.GOLDEN_PICKAXE),
    DIAMOND_PICKAXE(41, "Diamond Pickaxe", Material.DIAMOND_PICKAXE);

    private static final Map<Material, Pickaxe> PICKAXES = new EnumMap<>(Material.class);

    static {
        for (var requirement : values()) {
            if (PICKAXES.put(requirement.material, requirement) != null) {
                throw new IllegalStateException("Two (or more) of the same material [" + requirement.material +
                    "] reside in multiple enum values!");
            }
        }
    }

    /**
     * The mining level required to use this pickaxe.
     */
    private final int requiredMiningLevel;

    /**
     * The formatted name of this pickaxe.
     */
    private final String formattedName;

    /**
     * The {@link Material} that corresponds to this pickaxe.
     */
    private final Material material;

    /**
     * Creates a new {@link Pickaxe} with the corresponding mining level, formatted name, and material.
     *
     * @param requiredMiningLevel the mining level required to use this pickaxe.
     * @param formattedName       the formatted name of this pickaxe.
     * @param material            the material that corresponds to this pickaxe.
     */
    Pickaxe(int requiredMiningLevel, String formattedName, Material material) {
        this.requiredMiningLevel = requiredMiningLevel;
        this.formattedName = formattedName;
        this.material = material;
    }

    /**
     * Gets the mining level required to use this {@link Pickaxe pickaxe}.
     *
     * @return this pickaxe's mining level requirement as an {@code int}.
     */
    public int getRequiredMiningLevel() {
        return requiredMiningLevel;
    }

    /**
     * Gets the formatted name of this {@link Pickaxe pickaxe}.
     *
     * @return this pickaxe's formatted name.
     */
    public String getFormattedName() {
        return formattedName;
    }

    /**
     * Gets the {@link Optional}-wrapped {@link Pickaxe pickaxe} for the specified {@link Material material}.
     *
     * @param material the material to get the pickaxe for.
     * @return An {@link Optional} that may or may not contain a pickaxe.
     */
    public static Optional<Pickaxe> forMaterial(Material material) {
        return Optional.ofNullable(PICKAXES.get(material));
    }
}
