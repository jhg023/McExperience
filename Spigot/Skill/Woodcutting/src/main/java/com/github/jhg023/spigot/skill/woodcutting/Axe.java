package com.github.jhg023.spigot.skill.woodcutting;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * An enumeration of axes and their respective woodcutting level requirements.
 *
 * @author Jacob Glickman
 * @version January 9, 2020
 */
public enum Axe {

    WOODEN_AXE(1, "Wooden Axe", Material.WOODEN_AXE),
    STONE_AXE(6, "Stone Axe", Material.STONE_AXE),
    IRON_AXE(21, "Iron Axe", Material.IRON_AXE),
    GOLDEN_AXE(31, "Golden Axe", Material.GOLDEN_AXE),
    DIAMOND_AXE(41, "Diamond Axe", Material.DIAMOND_AXE);

    private static final Map<Material, Axe> AXES = new EnumMap<>(Material.class);

    static {
        for (var requirement : values()) {
            if (AXES.put(requirement.material, requirement) != null) {
                throw new IllegalStateException("Two (or more) of the same material [" + requirement.material +
                    "] reside in multiple enum values!");
            }
        }
    }

    /**
     * The woodcutting level required to use this axe.
     */
    private final int requiredWoodcuttingLevel;

    /**
     * The formatted name of this axe.
     */
    private final String formattedName;

    /**
     * The {@link Material} that corresponds to this axe.
     */
    private final Material material;

    /**
     * Creates a new {@link Axe} with the corresponding woodcutting level, formatted name, and material.
     *
     * @param requiredWoodcuttingLevel the woodcutting level required to use this axe.
     * @param formattedName            the formatted name of this axe.
     * @param material                 the material that corresponds to this axe.
     */
    Axe(int requiredWoodcuttingLevel, String formattedName, Material material) {
        this.requiredWoodcuttingLevel = requiredWoodcuttingLevel;
        this.formattedName = formattedName;
        this.material = material;
    }

    /**
     * Gets the woodcutting level required to use this {@link Axe axe}.
     *
     * @return this axe's woodcutting level requirement as an {@code int}.
     */
    public int getRequiredWoodcuttingLevel() {
        return requiredWoodcuttingLevel;
    }

    /**
     * Gets the formatted name of this {@link Axe axe}.
     *
     * @return this axe's formatted name.
     */
    public String getFormattedName() {
        return formattedName;
    }

    /**
     * Gets the {@link Optional}-wrapped {@link Axe axe} for the specified {@link Material material}.
     *
     * @param material the material to get the axe for.
     * @return An {@link Optional} that may or may not contain an axe.
     */
    public static Optional<Axe> forMaterial(Material material) {
        return Optional.ofNullable(AXES.get(material));
    }
}
