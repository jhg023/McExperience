package com.github.jhg023.spigot.skill.fishing;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * An enumeration of the materials that can be fished along with the corresponding experience values.
 *
 * @author Jacob G.
 * @version January 11, 2020
 */
public enum Fishable {

    // Fish
    RAW_COD(10, Material.COD),
    RAW_SALMON(24, Material.SALMON),
    TROPICAL_FISH(300, Material.TROPICAL_FISH),
    PUFFERFISH(47, Material.PUFFERFISH),

    // Treasure
    BOW(714, Material.BOW),
    ENCHANTED_BOOK(714, Material.ENCHANTED_BOOK),
    NAME_TAG(714, Material.NAME_TAG),
    NAUTILUS_SHELL(714, Material.NAUTILUS_SHELL),
    SADDLE(714, Material.SADDLE),
    LILY_PAD(714, Material.LILY_PAD),

    // Junk
    BOWL(425, Material.BOWL),
    FISHING_ROD(2_125, Material.FISHING_ROD),
    LEATHER(425, Material.LEATHER),
    LEATHER_BOOTS(425, Material.LEATHER_BOOTS),
    ROTTEN_FLESH(425, Material.ROTTEN_FLESH),
    STICK(850, Material.STICK),
    STRING(850, Material.STRING),
    WATER_BOTTLE(425, Material.GLASS_BOTTLE),
    BONE(425, Material.BONE),
    INK_SAC(4250, Material.INK_SAC),
    TRIPWIRE_HOOK(425, Material.TRIPWIRE_HOOK);

    /**
     * A {@code static} collection that stores {@link Material} mappings for constant-time access.
     */
    private static final Map<Material, Fishable> FISHABLES = new EnumMap<>(Material.class);

    static {
        for (var fishable : values()) {
            if (FISHABLES.put(fishable.material, fishable) != null) {
                throw new IllegalStateException("Two (or more) of the same material [" + fishable.material +
                    "] reside in multiple enum values!");
            }
        }
    }

    /**
     * The fishing experience that this {@link Fishable fishable} provides when fished.
     */
    private final int fishingExperience;

    /**
     * The {@link Material material} that corresponds to this {@link Fishable fishable}.
     */
    private final Material material;

    /**
     * Creates a new {@link Fishable fishable} with the corresponding fishing experience and material.
     *
     * @param fishingExperience the fishing experience that this fishable provides when fished.
     * @param material          the material that corresponds to this fishable.
     */
    Fishable(int fishingExperience, Material material) {
        this.fishingExperience = fishingExperience;
        this.material = material;
    }

    /**
     * Gets the amount of fishing experience provided by this {@link Fishable fishable} when fished.
     *
     * @return this fishable's provided fishing experience as an {@code int}.
     */
    public int getFishingExperience() {
        return fishingExperience;
    }

    /**
     * Gets an {@link Optional}-wrapped {@link Fishable} given a specified {@link Material}.
     *
     * @param material the material that is mapped to a {@link Fishable}.
     * @return An {@link Optional} that may or may not contain a {@link Fishable}.
     */
    public static Optional<Fishable> forMaterial(Material material) {
        return Optional.ofNullable(FISHABLES.get(material));
    }
}
