package de.zillolp.cookieclicker.database;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import de.zillolp.cookieclicker.CookieClicker;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class DatabaseConnector {
    public boolean disabled;
    private final String url;
    private final String driver;
    private final Properties properties;
    private Connection connection = null;

    public DatabaseConnector(boolean useMysql, String filename, String address, String port, String databaseName, String username, String password) {
        disabled = false;
        properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);
        properties.put("autoReconnect", true);
        if (useMysql) {
            driver = ("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://" + address + ":" + port + "/" + databaseName + "?autoReconnect=true";
            return;
        }
        CookieClicker cookieClicker = CookieClicker.cookieClicker;
        File databaseFile = new File(cookieClicker.getDataFolder(), filename + ".db");
        if (!databaseFile.exists()) {
            try {
                databaseFile.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        driver = ("org.sqlite.JDBC");
        url = "jdbc:sqlite:" + databaseFile.getAbsolutePath();
    }

    public void open() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, properties);
        } catch (SQLException exception) {
            System.out.println("[CookieClicker] Could not connect to MySQL server! Error: " + exception.getMessage());
        } catch (ClassNotFoundException exception) {
            System.out.println("[CookieClicker] JDBC Driver not found!");
            System.out.println("[CookieClicker] Could not connect to MySQL server! Error: " + exception.getMessage());
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void update(PreparedStatement preparedStatement) {
        if (disabled) {
            updateStatement(preparedStatement);
            return;
        }
        CompletableFuture.runAsync(() -> updateStatement(preparedStatement));
    }

    private void updateStatement(PreparedStatement preparedStatement) {
        if (!(checkConnection())) {
            return;
        }
        try {
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public PreparedStatement prepareStatement(String sql) {
        if (!(checkConnection())) {
            return null;
        }
        try {
            return connection.prepareStatement(sql);
        } catch (CommunicationsException exception) {
            close();
            open();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean checkConnection() {
        return connection != null;
    }
}
