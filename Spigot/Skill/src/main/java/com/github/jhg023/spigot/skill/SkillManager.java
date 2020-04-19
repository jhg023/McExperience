package com.github.jhg023.spigot.skill;

import com.github.jhg023.spigot.database.Database;
import com.github.jhg023.spigot.skill.event.PlayerSkillLevelUpEvent;
import com.github.jhg023.spigot.skill.event.SkillEventHandler;
import com.github.jhg023.spigot.skill.utility.SkillUtility;
import com.github.jhg023.spigot.skill.command.SkillCommandHandler;
import com.github.jhg023.spigot.skill.utility.MutableInt;
import com.github.jhg023.spigot.skill.utility.SkillExperience;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.logging.Level;

/**
 * The plugin that manages a {@link Player player}'s per-skill experience and periodically publishes it to the database.
 *
 * @author Jacob Glickman
 * @version January 5, 2020
 */
public final class SkillManager extends JavaPlugin {

    private static final String SQL_EXCEPTION_MESSAGE = "A SQLException occurred when updating a player's tracked " +
        "skill!";

    /**
     * A singleton instance of {@link SkillManager}.
     */
    private static SkillManager skillManager;

    /**
     * The data structure that stores players that are currently tracking a skill on their screen.
     */
    private final ConcurrentMap<UUID, SkillTracker> trackerMap = new ConcurrentHashMap<>();

    /**
     * The in-memory data structure that stores every {@link Player player}'s current experience values for every
     * {@link Skill skill}.
     */
    private final ConcurrentMap<UUID, Map<Skill, MutableInt>> experienceMap = new ConcurrentHashMap<>();

    /**
     * The in-memory data structure that stores pending experience that has not yet been published to the database.
     */
    private final ConcurrentMap<UUID, Map<Skill, MutableInt>> pendingExperience = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        skillManager = this;

        Objects.requireNonNull(getCommand("track")).setExecutor(new SkillCommandHandler(this));

        getServer().getPluginManager().registerEvents(new SkillEventHandler(this, trackerMap, experienceMap), this);
        getServer().getScheduler().runTaskTimerAsynchronously(this,
            () -> Database.getMySQL().connect((Consumer<Connection>) this::addExperienceHelper), 0L, 20L * 30L);
    }

    @Override
    public void onDisable() {
        getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',
            "&f[&aSkill&f] Plugin is being disabled; expect temporary lag!"));
        Database.getMySQL().connect((Consumer<Connection>) skillManager::addExperienceHelper);
        getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',
            "&f[&aSkill&f] Plugin has been disabled!"));
    }

    /**
     * Gives the specified amount of experience in the specified skill to the specified {@link Player player}.
     * <br><br>
     * If the specified amount of experience is less than or equal to {@code 0}, nothing happens.
     *
     * @param player the player to give the experience to.
     * @param skill  the skill to give the experience in.
     * @param amount the amount of experience to give.
     */
    public void addExperience(Player player, Skill skill, int amount) {
        if (amount <= 0) {
            return;
        }

        // TODO: Add mechanism for double experience events/weekends.

        var uuid = player.getUniqueId();

        experienceMap.computeIfPresent(uuid, (UUID key, Map<Skill, MutableInt> skills) -> {
            int oldExperience = skills.get(skill).get();
            int oldLevel = SkillUtility.getLevelForExperience(oldExperience);
            int newLevel = SkillUtility.getLevelForExperience(oldExperience + amount);

            if (oldLevel != newLevel) {
                getServer().getPluginManager().callEvent(
                    new PlayerSkillLevelUpEvent(getServer().getPlayer(uuid), skill, newLevel));
            }

            pendingExperience.compute(uuid, (unused, value) -> addExperienceHelper(skill, amount, value));

            Map<Skill, MutableInt> updatedExperienceMap = addExperienceHelper(skill, amount, skills);
            updateTracker(player, skill);
            return updatedExperienceMap;
        });
    }

    /**
     * Publishes all pending experience updates for all {@link Player}s to the database.
     *
     * @param connection a connection to the database.
     */
    private void addExperienceHelper(Connection connection) {
        try (var statement = connection.createStatement()) {
            for (var uuid : pendingExperience.keySet()) {
                pendingExperience.computeIfPresent(uuid, (UUID key, Map<Skill, MutableInt> value) -> {
                    try {
                        addToBatch(uuid, value, statement);
                    } catch (SQLException e) {
                        String message = "A SQLException occurred when adding queries to a batch!";
                        Bukkit.getLogger().log(Level.SEVERE, message, e);
                    }

                    return null;
                });
            }

            statement.executeBatch();
        } catch (SQLException e) {
            String message = "A SQLException occurred when updating all players' experience!";
            Bukkit.getLogger().log(Level.SEVERE, message, e);
        }
    }

    /**
     * A helper method for {@link #addExperience(Player, Skill, int)} to reduce duplicate code.
     *
     * @param skill  the skill to give the experience to.
     * @param amount the amount of experience to give.
     * @param skills a mapping of skills to their respective amount of experience.
     * @return a new or modified map, depending on the initial state of {@code skills}.
     */
    private Map<Skill, MutableInt> addExperienceHelper(Skill skill, int amount, Map<Skill, MutableInt> skills) {
        if (skills == null) {
            return new EnumMap<>(Map.of(skill, new MutableInt(amount)));
        }

        skills.merge(skill, new MutableInt(amount), (oldValue, unused) -> oldValue.add(amount));

        return skills;
    }

    /**
     * A helper method to reduce duplicate code in {@link #addExperienceHelper(Connection)}.
     *
     * @param uuid   the unique identifier of the player to update the experience for.
     * @param skills a map containing the skills to update, as well as the respective experience increments.
     */
    private void addToBatch(UUID uuid, Map<Skill, MutableInt> skills, Statement statement) throws SQLException {
        for (var entry : skills.entrySet()) {
            String column = '`' + entry.getKey().name().toLowerCase(Locale.US) + "_skill_exp`";
            statement.addBatch("UPDATE `survival_player_skill_data` SET " + column + " = " + column + " + " +
                entry.getValue().get() + " WHERE `uuid` = '" + uuid + "';");
        }
    }

    /**
     * Creates an experience orb that provides the specified amount of experience in the specified
     * {@link Skill skill} at the specified {@link Location location}.
     * <br><br>
     * The skill experience will be rewarded to whichever player picks up the orb, which may not be the
     * {@link Player player} that the orb was created for.
     *
     * @param player     The player that prompted the creation of this orb.
     * @param location   The location at which to spawn the orb.
     * @param skill      The skill to reward the experience in.
     * @param experience The amount of experience in the specified skill that the orb should provide.
     */
    public void createExperienceOrb(Player player, Location location, Skill skill, int experience) {
        player.getWorld().spawn(location, ExperienceOrb.class, (ExperienceOrb orb) -> {
            orb.setExperience(0);
            orb.setMetadata(SkillUtility.SKILL_METADATA_KEY,
                new FixedMetadataValue(this, new SkillExperience(skill, experience)));
        });
    }

    /**
     * Determines whether or not a {@link Player player} is allowed to perform a specific action depending on if their
     * level in the skill specified is greater than or equal to the specified required level.
     *
     * @param player        the player.
     * @param skill         the skill that corresponds to the specific action.
     * @param requiredLevel the level required to perform a specific action.
     * @param function      supplies the text stating what the player can do if they reach the required level (i.e.
     *                      use a Diamond Pickaxe!).
     * @return {@code true} if the player is <strong>NOT</strong> allowed to perform a specific action, otherwise
     *         {@code false}.
     */
    public boolean doesNotMeetLevelRequirement(Player player, Skill skill, int requiredLevel,
                                               IntFunction<String> function) {
        int level = SkillUtility.getLevelForExperience(experienceMap.get(player.getUniqueId()).get(skill).get());

        if (level < requiredLevel) {
            player.sendActionBar('&', "A &a" + skill.getDisplay() + "&f level of &a" +
                requiredLevel + " &fis required to " + function.apply(requiredLevel));
            return true;
        }

        return false;
    }

    /**
     * Updates a specified {@link Player}'s tracker with a tracker for a specified {@link Skill}.
     * <br><br>
     * If the specified skill is the same skill that the player is already tracking, then the tracker's progress is
     * simply updated. Otherwise, the player is removed from the old tracker and a new tracker is created with the
     * new skill.
     *
     * @param player the player to update the tracker for.
     * @param skill  the skill to use for the updated tracker.
     */
    public void updateTracker(Player player, Skill skill) {
        trackerMap.compute(player.getUniqueId(), (UUID uuid, SkillTracker tracker) -> {
            var xp = experienceMap.get(uuid).get(skill).get();
            var level = SkillUtility.getLevelForExperience(xp);

            if (tracker != null) {
                if (tracker.getSkill() == skill) {
                    var bar = tracker.getBar();

                    bar.setProgress(SkillUtility.getProgress(xp, level));
                    bar.setTitle(SkillUtility.formatDisplay(skill, xp));

                    return tracker;
                }

                tracker.getBar().removePlayer(player);
            }

            Bukkit.getScheduler().runTaskAsynchronously(skillManager, () -> {
                Database.getMySQL().connect((Connection connection) -> {
                    try (var statement = connection.createStatement()) {
                        statement.executeUpdate("UPDATE `survival_player_skill_data` SET `tracked_skill` = '" +
                            skill.name() + "' WHERE `uuid` = '" + uuid + "';");
                    } catch (SQLException e) {
                        Bukkit.getLogger().log(Level.SEVERE, SQL_EXCEPTION_MESSAGE, e);
                    }
                }, exception -> Bukkit.getLogger().log(Level.SEVERE, SQL_EXCEPTION_MESSAGE, exception));
            });

            var bar = Bukkit.createBossBar(SkillUtility.formatDisplay(skill, xp), BarColor.WHITE, BarStyle.SOLID);

            bar.addPlayer(player);
            bar.setProgress(SkillUtility.getProgress(xp, level));
            bar.setVisible(true);

            return new SkillTracker(skill, bar);
        });
    }

    /**
     * Gets a singleton instance of {@link SkillManager}.
     *
     * @return A {@link SkillManager} instance.
     */
    public static SkillManager getInstance() {
        return skillManager;
    }
}
