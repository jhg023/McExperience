package com.github.jhg023.spigot.skill.utility;

/**
 * A wrapper object that represents a mutable {@code int} that can be stored in collections.
 *
 * @author Jacob Glickman
 * @version January 5, 2020
 */
public final class MutableInt {

    /**
     * The backing {@code int} of this {@link MutableInt}.
     */
    private int value;

    /**
     * Constructs a new {@link MutableInt} with its initial value set to {@code 0}.
     */
    public MutableInt() {
        this(0);
    }

    /**
     * Constructs a new {@link MutableInt} with its initial value set to {@code value}.
     *
     * @param value the initial value of this {@link MutableInt}.
     */
    public MutableInt(int value) {
        this.value = value;
    }

    /**
     * Adds a specified value to this {@link MutableInt}.
     *
     * @param value the value to add.
     * @return this {@link MutableInt} for the convenience of method-chaining.
     */
    public MutableInt add(int value) {
        this.value += value;
        return this;
    }

    /**
     * Gets the value of this {@link MutableInt} as an {@code int}.
     *
     * @return the value of this {@link MutableInt}.
     */
    public int get() {
        return value;
    }

    /**
     * Sets the value of this {@link MutableInt} to a specified {@code int} value.
     *
     * @param value the value to set this {@link MutableInt} to.
     */
    public void set(int value) {
        this.value = value;
    }
}
