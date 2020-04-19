package com.github.jhg023.spigot.database;

import com.github.jhg023.common.database.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

public final class Database extends JavaPlugin {

    private static MySQL mySQL;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        var config = getConfig();

        mySQL = new MySQL(config.getString("mysql.host"), config.getInt("mysql.port"),
            config.getString("mysql.database"), config.getString("mysql.username"), config.getString("mysql.password"));
    }

    @Override
    public void onDisable() {
        mySQL.close();
    }

    public static MySQL getMySQL() {
        return mySQL;
    }
}
