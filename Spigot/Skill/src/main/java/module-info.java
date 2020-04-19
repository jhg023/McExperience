module com.github.jhg023.spigot.skill {
    requires transitive java.logging;

    requires com.github.jhg023.spigot.database;
    requires org.bukkit;

    exports com.github.jhg023.spigot.skill;
    exports com.github.jhg023.spigot.skill.utility;
}
