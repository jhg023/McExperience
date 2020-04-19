package com.github.jhg023.spigot.skill.attack;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * An enumeration of swords and their respective attack level requirements.
 *
 * @author Jacob Glickman
 * @version January 11, 2020
 */
public enum Weapon {

    WOODEN_AXE(1, "Wooden Axe", Material.WOODEN_AXE),
    WOODEN_HOE(1, "Wooden Hoe", Material.WOODEN_HOE),
    WOODEN_PICKAXE(1, "Wooden Pickaxe", Material.WOODEN_PICKAXE),
    WOODEN_SHOVEL(1, "Wooden Shovel", Material.WOODEN_SHOVEL),
    WOODEN_SWORD(1, "Wooden Sword", Material.WOODEN_SWORD),

    STONE_AXE(6, "Stone Axe", Material.STONE_AXE),
    STONE_HOE(6, "Stone Hoe", Material.STONE_HOE),
    STONE_PICKAXE(6, "Stone Pickaxe", Material.STONE_PICKAXE),
    STONE_SHOVEL(6, "Stone Shovel", Material.STONE_SHOVEL),
    STONE_SWORD(6, "Stone Sword", Material.STONE_SWORD),

    IRON_AXE(21, "Iron Axe", Material.IRON_AXE),
    IRON_HOE(21, "Iron Hoe", Material.IRON_HOE),
    IRON_PICKAXE(21, "Iron Pickaxe", Material.IRON_PICKAXE),
    IRON_SHOVEL(21, "Iron Shovel", Material.IRON_SHOVEL),
    IRON_SWORD(21, "Iron Sword", Material.IRON_SWORD),

    GOLDEN_AXE(31, "Golden Axe", Material.GOLDEN_AXE),
    GOLDEN_HOE(31, "Golden Hoe", Material.GOLDEN_HOE),
    GOLDEN_PICKAXE(31, "Golden Pickaxe", Material.GOLDEN_PICKAXE),
    GOLDEN_SHOVEL(31, "Golden Shovel", Material.GOLDEN_SHOVEL),
    GOLDEN_SWORD(31, "Golden Sword", Material.GOLDEN_SWORD),

    DIAMOND_AXE(41, "Diamond Axe", Material.DIAMOND_AXE),
    DIAMOND_HOE(41, "Diamond Hoe", Material.DIAMOND_HOE),
    DIAMOND_PICKAXE(41, "Diamond Pickaxe", Material.DIAMOND_PICKAXE),
    DIAMOND_SHOVEL(41, "Diamond Shovel", Material.DIAMOND_SHOVEL),
    DIAMOND_SWORD(41, "Diamond Sword", Material.DIAMOND_SWORD);

    private static final Map<Material, Weapon> WEAPONS = new EnumMap<>(Material.class);

    static {
        for (var weapon : values()) {
            if (WEAPONS.put(weapon.material, weapon) != null) {
                throw new IllegalStateException("Two (or more) of the same material [" + weapon.material +
                    "] reside in multiple enum values!");
            }
        }
    }

    /**
     * The attack level required to use this weapon.
     */
    private final int requiredAttackLevel;

    /**
     * The formatted name of this weapon.
     */
    private final String formattedName;

    /**
     * The {@link Material} that corresponds to this weapon.
     */
    private final Material material;

    /**
     * Creates a new {@link Weapon} with the corresponding attack level, formatted name, and material.
     *
     * @param requiredAttackLevel the attack level required to use this weapon.
     * @param formattedName       the formatted name of this weapon.
     * @param material            the material that corresponds to this weapon.
     */
    Weapon(int requiredAttackLevel, String formattedName, Material material) {
        this.requiredAttackLevel = requiredAttackLevel;
        this.formattedName = formattedName;
        this.material = material;
    }

    /**
     * Gets the attack level required to use this {@link Weapon weapon}.
     *
     * @return this weapon's attack level requirement as an {@code int}.
     */
    public int getRequiredAttackLevel() {
        return requiredAttackLevel;
    }

    /**
     * Gets the formatted name of this {@link Weapon weapon}.
     *
     * @return this weapon's formatted name.
     */
    public String getFormattedName() {
        return formattedName;
    }

    /**
     * Gets the {@link Optional}-wrapped {@link Weapon weapon} for the specified {@link Material material}.
     *
     * @param material the material to get the weapon for.
     * @return An {@link Optional} that may or may not contain a weapon.
     */
    public static Optional<Weapon> forMaterial(Material material) {
        return Optional.ofNullable(WEAPONS.get(material));
    }
}
