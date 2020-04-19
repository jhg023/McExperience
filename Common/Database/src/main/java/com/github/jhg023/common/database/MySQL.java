package com.github.jhg023.common.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Used to distribute MySQL database connections from a Hikari connection pool.
 * <br><br>
 * To prevent connections from leaking accidentally, no {@link Connection} objects are exposed beyond this class. If
 * you happen to create a {@link Connection} object within this class, <strong>ALWAYS</strong> use a
 * try-with-resources statement to ensure that the connection is closed when it goes out-of-scope.
 *
 * @author Jacob Glickman
 * @version January 4, 2020
 */
public final class MySQL implements Closeable {

    /**
     * A representation of the standard format of MySQL JDBC URLs.
     */
    private static final String MYSQL_JDBC_URL_FORMAT = "jdbc:mysql://%s:%d/%s";

    /**
     * The backing connection pool.
     */
    private final HikariDataSource hikariDataSource;

    /**
     * Instantiates a new {@link MySQL} instance.
     *
     * @param host     The host.
     * @param port     The port.
     * @param database The database name.
     * @param username The username.
     * @param password The password.
     */
    public MySQL(String host, int port, String database, String username, String password) {
        var config = new HikariConfig();

        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setJdbcUrl(String.format(MYSQL_JDBC_URL_FORMAT, host, port, database));
        config.setUsername(username);
        config.setPassword(password);
        config.setMaxLifetime(TimeUnit.MILLISECONDS.convert(10L, TimeUnit.MINUTES));
        config.setAutoCommit(false);

        this.hikariDataSource = new HikariDataSource(config);
    }

    /**
     * Grabs a {@link Connection} object from the pool and accepts a user-specified {@link Function}.
     * <br><br>
     * The {@link Connection} object is <strong>not</strong> returned by this method to avoid any possibility of a
     * connection leak.
     * <br><br>
     * Any uncommitted transactions are committed when the {@link Connection} object goes out of scope.
     * <br><br>
     * This method will block the executing thread.
     *
     * @param onConnect The action to be performed if a connection is established to the database.
     * @return The result of {@code onConnect} if a connection is established to the database, otherwise {@code null}.
     */
    public <T> T connect(Function<Connection, T> onConnect) {
        return connect(onConnect, Throwable::printStackTrace);
    }

    /**
     * Grabs a {@link Connection} object from the pool and accepts a user-specified {@link Function}.
     * <br><br>
     * The {@link Connection} object is <strong>not</strong> returned by this method to avoid any possibility of a
     * connection leak.
     * <br><br>
     * Any uncommitted transactions are committed when the {@link Connection} object goes out of scope.
     * <br><br>
     * This method will block the executing thread.
     *
     * @param onConnect The action to be performed if a connection is established to the database.
     * @param onFailure The action to be performed if a {@link SQLException} occurs.
     * @return The result of {@code onConnect} if a connection is established to the database, otherwise {@code null}.
     */
    public <T> T connect(Function<Connection, T> onConnect, Consumer<SQLException> onFailure) {
        try (var connection = hikariDataSource.getConnection()) {
            T result = onConnect.apply(connection);
            connection.commit();
            return result;
        } catch (SQLException e) {
            onFailure.accept(e);
            return null;
        }
    }

    /**
     * Grabs a {@link Connection} object from the pool and accepts a user-specified {@link Consumer}.
     * <br><br>
     * The {@link Connection} object is <strong>not</strong> returned by this method to avoid any possibility of a
     * connection leak.
     * <br><br>
     * Any uncommitted transactions are committed when the {@link Connection} object goes out of scope.
     * <br><br>
     * This method will block the executing thread.
     *
     * @param onConnect The action to be performed if a connection is established to the database.
     */
    public void connect(Consumer<Connection> onConnect) {
        connect(onConnect, Throwable::printStackTrace);
    }

    /**
     * Grabs a {@link Connection} object from the pool and accepts a user-specified {@link Consumer}.
     * <br><br>
     * The {@link Connection} object is <strong>not</strong> returned by this method to avoid any possibility of a
     * connection leak.
     * <br><br>
     * Any uncommitted transactions are committed when the {@link Connection} object goes out of scope.
     * <br><br>
     * This method will block the executing thread.
     *
     * @param onConnect The action to be performed if a connection is established to the database.
     * @param onFailure The action to be performed if a {@link SQLException} occurs.
     */
    public void connect(Consumer<Connection> onConnect, Consumer<SQLException> onFailure) {
        connect((Connection connection) -> {
            onConnect.accept(connection);
            return null;
        }, onFailure);
    }

    /**
     * Returns a {@link CompletableFuture} that invokes {@link #connect(Function)} asynchronously.
     *
     * @param onConnect The action to be performed if a connection is established to the database.
     * @return A {@link CompletableFuture} with the result of invoking {@link #connect(Function)}.
     */
    public <T> CompletableFuture<T> connectAsync(Function<Connection, T> onConnect) {
        return CompletableFuture.supplyAsync(() -> connect(onConnect));
    }

    /**
     * Returns a {@link CompletableFuture} that invokes {@link #connect(Function, Consumer)} asynchronously.
     *
     * @param onConnect The action to be performed if a connection is established to the database.
     * @param onFailure The action to be performed if a {@link SQLException} occurs.
     * @return A {@link CompletableFuture} with the result of invoking {@link #connect(Function, Consumer)}.
     */
    public <T> CompletableFuture<T> connectAsync(Function<Connection, T> onConnect, Consumer<SQLException> onFailure) {
        return CompletableFuture.supplyAsync(() -> connect(onConnect, onFailure));
    }

    /**
     * Returns a {@link CompletableFuture} that invokes {@link #connect(Consumer)} asynchronously.
     *
     * @param onConnect The action to be performed if a connection is established to the database.
     * @return A {@link CompletableFuture}.
     */
    public CompletableFuture<Void> connectAsync(Consumer<Connection> onConnect) {
        return CompletableFuture.runAsync(() -> connect(onConnect));
    }

    /**
     * Returns a {@link CompletableFuture} that invokes {@link #connect(Consumer, Consumer)} asynchronously.
     *
     * @param onConnect The action to be performed if a connection is established to the database.
     * @param onFailure The action to be performed if a {@link SQLException} occurs.
     * @return A {@link CompletableFuture}.
     */
    public CompletableFuture<Void> connectAsync(Consumer<Connection> onConnect, Consumer<SQLException> onFailure) {
        return CompletableFuture.runAsync(() -> connect(onConnect, onFailure));
    }

    /**
     * Closes the backing {@link HikariDataSource}.
     */
    @Override
    public void close() {
        hikariDataSource.close();
    }
}
