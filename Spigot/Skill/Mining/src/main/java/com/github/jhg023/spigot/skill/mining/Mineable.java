package com.github.jhg023.spigot.skill.mining;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * An enumeration of the {@link Material materials} that can be mined, along with their corresponding experience values.
 *
 * @author Jacob Glickman
 * @version January 6, 2020
 */
public enum Mineable {

    STONE(1, 1,Material.STONE, "Stone"),
    SANDSTONE(1, 1, Material.SANDSTONE, "Sandstone"),
    ANDESITE(5, 2, Material.ANDESITE, "Andesite"),
    DIORITE(5, 2, Material.DIORITE, "Diorite"),
    GRANITE(5, 2, Material.GRANITE, "Granite"),
    IRON_ORE(15, 35, Material.IRON_ORE, "Iron Ore"),
    NETHER_QUARTZ_ORE(20, 40, Material.NETHER_QUARTZ_ORE, "Nether Quartz Ore"),
    COAL_ORE(30, 50, Material.COAL_ORE, "Core Ore"),
    GOLD_ORE(40, 65, Material.GOLD_ORE, "Gold Ore"),
    LAPIS_LAZULI_ORE(55,  80, Material.LAPIS_ORE, "Lapis Lazuli Ore"),
    REDSTONE_ORE(65, 90, Material.REDSTONE_ORE, "Redstone Ore"),
    EMERALD_ORE(70, 95, Material.EMERALD_ORE, "Emerald Ore"),
    DIAMOND_ORE(85, 125, Material.DIAMOND_ORE, "Diamond Ore");

    /**
     * A {@code static} collection that stores {@link Material} mappings for constant-time access.
     */
    private static final Map<Material, Mineable> MINEABLES = new EnumMap<>(Material.class);

    static {
        for (var value : values()) {
            if (MINEABLES.put(value.material, value) != null) {
                throw new IllegalStateException("Two (or more) of the same material [" + value.material +
                    "] reside in multiple enum values!");
            }
        }
    }

    /**
     * The mining level required to be able to mine this block.
     */
    private final int requiredMiningLevel;

    /**
     * The mining experience that this block provides when mined.
     */
    private final int miningExperience;

    /**
     * The {@link Material material} that corresponds to this block.
     */
    private final Material material;

    /**
     * The formatted name of this block.
     */
    private final String formattedName;

    /**
     * Creates a new {@link Mineable} with the corresponding mining level, mining experience, and material.
     *
     * @param requiredMiningLevel the mining level required to mine this block.
     * @param miningExperience    the mining experience that this block provides when mined.
     * @param material            the material that corresponds to this block.
     * @param formattedName       the formatted name of this block.
     */
    Mineable(int requiredMiningLevel, int miningExperience, Material material, String formattedName) {
        this.requiredMiningLevel = requiredMiningLevel;
        this.miningExperience = miningExperience;
        this.material = material;
        this.formattedName = formattedName;
    }

    /**
     * Gets the mining level required to mine this block.
     *
     * @return this block's required mining level as an {@code int}.
     */
    public int getRequiredMiningLevel() {
        return requiredMiningLevel;
    }

    /**
     * Gets the amount of mining experience provided by this block when mined with a pickaxe.
     *
     * @return this block's provided mining experience as an {@code int}.
     */
    public int getMiningExperience() {
        return miningExperience;
    }

    /**
     * Gets the formatted name of this block.
     *
     * @return this block's formatted name.
     */
    public String getFormattedName() {
        return formattedName;
    }

    /**
     * Gets an {@link Optional}-wrapped {@link Mineable} given a specified {@link Material}.
     *
     * @param material the material that is mapped to a {@link Mineable}.
     * @return An {@link Optional} that may or may not contain a {@link Mineable}.
     */
    public static Optional<Mineable> forMaterial(Material material) {
        return Optional.ofNullable(MINEABLES.get(material));
    }
}
