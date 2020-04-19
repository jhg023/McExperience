package com.github.jhg023.spigot.skill.event;

import com.destroystokyo.paper.event.entity.ExperienceOrbMergeEvent;
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import com.github.jhg023.spigot.database.Database;
import com.github.jhg023.spigot.skill.Skill;
import com.github.jhg023.spigot.skill.SkillTracker;
import com.github.jhg023.spigot.skill.SkillManager;
import com.github.jhg023.spigot.skill.utility.MutableInt;
import com.github.jhg023.spigot.skill.utility.SkillExperience;
import com.github.jhg023.spigot.skill.utility.SkillUtility;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.MetadataValue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

/**
 * The class that handles skill-related events for the SkillManager module.
 *
 * @author Jacob Glickman
 * @version January 5, 2020
 */
public final class SkillEventHandler implements Listener {

    private final SkillManager manager;

    private final ConcurrentMap<UUID, SkillTracker> trackerMap;

    private final ConcurrentMap<UUID, Map<Skill, MutableInt>> experienceMap;

    public SkillEventHandler(SkillManager manager, ConcurrentMap<UUID, SkillTracker> trackerMap,
                             ConcurrentMap<UUID, Map<Skill, MutableInt>> experienceMap) {
        this.manager = manager;
        this.trackerMap = trackerMap;
        this.experienceMap = experienceMap;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        var uuid = player.getUniqueId();

        manager.getServer().getScheduler().runTaskAsynchronously(manager, () -> {
            Database.getMySQL().connect((Connection connection) -> {
                boolean exists;

                try (var statement = connection.createStatement()) {
                    exists = statement.executeUpdate("INSERT IGNORE INTO `survival_player_skill_data`(`uuid`) " +
                        "VALUES ('" + uuid + "');") == 0;
                    connection.commit();
                } catch (SQLException e) {
                    String message = "A SQLException occurred when searching if a player exists in a skill table!";
                    Bukkit.getLogger().log(Level.SEVERE, message, e);
                    return;
                }

                Skill trackedSkill = Skill.MINING;

                var playerExperienceMap = new EnumMap<Skill, MutableInt>(Skill.class);

                if (!exists) {
                    Arrays.stream(Skill.values()).forEach(skill -> playerExperienceMap.put(skill, new MutableInt()));
                } else {
                    try (var statement = connection.createStatement()) {
                        var resultSet = statement.executeQuery("SELECT * FROM `survival_player_skill_data` " +
                            "WHERE `uuid` = '" + uuid + "';");

                        if (!resultSet.next()) {
                            String message = "An impossible error has occurred!";
                            Bukkit.getLogger().log(Level.SEVERE, message);
                            return;
                        }

                        for (var skill : Skill.values()) {
                            int experience = resultSet.getInt(skill.getDatabaseColumnName());
                            playerExperienceMap.put(skill, new MutableInt(experience));
                        }

                        trackedSkill = Skill.valueOf(resultSet.getString("tracked_skill"));
                    } catch (SQLException e) {
                        String message = "A SQLException occurred when grabbing a player's data!";
                        Bukkit.getLogger().log(Level.SEVERE, message, e);
                        return;
                    }
                }

                experienceMap.put(uuid, playerExperienceMap);
                manager.updateTracker(player, trackedSkill);
            });
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        var uuid = event.getPlayer().getUniqueId();

        trackerMap.remove(uuid);
        experienceMap.remove(uuid);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSkillLevelUp(PlayerSkillLevelUpEvent event) {
        var player = event.getPlayer();
        var skill = event.getSkill();

        // TODO: Send firework.
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3F, 1F);

        trackerMap.compute(player.getUniqueId(), (UUID uuid, SkillTracker tracker) -> {
            if (tracker != null) {
                tracker.getBar().setTitle(SkillUtility.formatDisplay(skill, experienceMap.get(uuid).get(skill).get()));
            }

            return tracker;
        });
    }

    @EventHandler
    public void onPlayerPickupExperience(PlayerPickupExperienceEvent event) {
        var orb = event.getExperienceOrb();

        // If the experience does not have skill metadata, return.
        if (!orb.hasMetadata(SkillUtility.SKILL_METADATA_KEY)) {
            return;
        }

        orb.getMetadata(SkillUtility.SKILL_METADATA_KEY).stream()
            .filter(metadataValue -> metadataValue.getOwningPlugin() == manager)
            .map(MetadataValue::value)
            .filter(value -> value instanceof SkillExperience)
            .map(value -> (SkillExperience) value)
            .forEach(skillExperience -> manager.addExperience(event.getPlayer(),
                skillExperience.getSkill(), skillExperience.getExperience()));
    }

    @EventHandler
    public void onExperienceOrbMerge(ExperienceOrbMergeEvent event) {
        event.setCancelled(true);
    }
}
