package com.github.jhg023.spigot.skill.woodcutting;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * An enumeration of the {@link Material materials} that can be chopped, along with their corresponding experience
 * values.
 *
 * @author Jacob Glickman
 * @version January 9, 2020
 */
public enum Log {

    OAK_LOG(1, 3, "Oak Log", Material.OAK_LOG),
    STRIPPED_OAK_LOG(7, 6, "Stripped Oak Log", Material.STRIPPED_OAK_LOG),

    SPRUCE_LOG(15, 5, "Spruce Logs", Material.SPRUCE_LOG),
    STRIPPED_SPRUCE_LOG(22, 10, "Stripped Spruce Log", Material.STRIPPED_SPRUCE_LOG),

    BIRCH_LOG(30, 9, "Birch Log", Material.BIRCH_LOG),
    STRIPPED_BIRCH_LOG(37, 18, "Stripped Birch Log", Material.STRIPPED_BIRCH_LOG),

    JUNGLE_LOG(45, 17, "Jungle Log", Material.JUNGLE_LOG),
    STRIPPED_JUNGLE_LOG(52, 34, "Stripped Jungle Log", Material.STRIPPED_JUNGLE_LOG),

    ACACIA_LOG(60, 33, "Acacia Log", Material.ACACIA_LOG),
    STRIPPED_ACACIA_LOG(67, 66, "Stripped Acacia Log", Material.STRIPPED_ACACIA_LOG),

    DARK_OAK_LOG(75, 65, "Dark Oak Log", Material.DARK_OAK_LOG),
    STRIPPED_DARK_OAK_LOG(82, 130, "Stripped Dark Oak Log", Material.STRIPPED_DARK_OAK_LOG);

    /**
     * A {@code static} collection that stores {@link Material} mappings for constant-time access.
     */
    private static final Map<Material, Log> LOGS = new EnumMap<>(Material.class);

    static {
        for (var value : values()) {
            if (LOGS.put(value.material, value) != null) {
                throw new IllegalStateException("Two (or more) of the same material [" + value.material +
                    "] reside in multiple enum values!");
            }
        }
    }

    /**
     * The woodcutting level required to chop this log.
     */
    private final int requiredWoodcuttingLevel;

    /**
     * The woodcutting experience that this log provides when chopped.
     */
    private final int woodcuttingExperience;

    /**
     * The formatted name of this log.
     */
    private final String formattedName;

    /**
     * The {@link Material} that corresponds to this log.
     */
    private final Material material;

    /**
     * Creates a new {@link Log} with the corresponding woodcutting level, woodcutting experience, and material.
     *
     * @param requiredWoodcuttingLevel the woodcutting level required to chop this log.
     * @param woodcuttingExperience    the woodcutting experience that this log provides when chopped.
     * @param formattedName            the formatted name of this block.
     * @param material                 the material that corresponds to this block.
     */
    Log(int requiredWoodcuttingLevel, int woodcuttingExperience, String formattedName, Material material) {
        this.requiredWoodcuttingLevel = requiredWoodcuttingLevel;
        this.woodcuttingExperience = woodcuttingExperience;
        this.formattedName = formattedName;
        this.material = material;
    }

    /**
     * Gets the woodcutting level required to chop this log with an axe.
     *
     * @return this log's required woodcutting level as an {@code int}.
     */
    public int getRequiredWoodcuttingLevel() {
        return requiredWoodcuttingLevel;
    }

    /**
     * Gets the amount of woodcutting experience provided by this log when chopped with an axe.
     *
     * @return this block's provided woodcutting experience as an {@code int}.
     */
    public int getWoodcuttingExperience() {
        return woodcuttingExperience;
    }

    /**
     * Gets the formatted name of this log.
     *
     * @return this log's formatted name.
     */
    public String getFormattedName() {
        return formattedName;
    }

    /**
     * Gets the {@link Optional}-wrapped {@link Log log} for the specified {@link Material material}.
     *
     * @param material the material to get the log for.
     * @return An {@link Optional} that may or may not contain a log.
     */
    public static Optional<Log> forMaterial(Material material) {
        return Optional.ofNullable(LOGS.get(material));
    }
}
