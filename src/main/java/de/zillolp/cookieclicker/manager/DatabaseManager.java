package de.zillolp.cookieclicker.manager;

import de.zillolp.cookieclicker.CookieClicker;
import de.zillolp.cookieclicker.database.DatabaseConnector;
import de.zillolp.cookieclicker.enums.Designs;
import de.zillolp.cookieclicker.profiles.PlayerProfile;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.UUID;

public class DatabaseManager {
    private final CookieClicker cookieClicker = CookieClicker.cookieClicker;
    private final DatabaseConnector databaseConnector;

    public DatabaseManager(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
        databaseConnector.update(databaseConnector.prepareStatement("CREATE TABLE IF NOT EXISTS cookieclicker_players(UUID varchar(64), NAME varchar(64), COOKIES bigint, PER_CLICK bigint, DESIGN varchar(16), CLICKER_CLICKS bigint, " +
                "PRICE bigint, PRICE1 bigint, PRICE2 bigint, PRICE3 bigint, PRICE4 bigint, PRICE5 bigint, PRICE6 bigint, PRICE7 bigint, PRICE8 bigint, PRICE9 bigint, PRICE10 bigint, PRICE11 bigint, PRICE12 bigint, PRICE13 bigint);"));
    }

    public void createPlayer(UUID uuid, String name) {
        try {
            PreparedStatement statement = databaseConnector.prepareStatement("INSERT INTO cookieclicker_players(UUID, NAME, COOKIES, PER_CLICK, DESIGN, CLICKER_CLICKS, " +
                    "PRICE, PRICE1, PRICE2, PRICE3, PRICE4, PRICE5, PRICE6, PRICE7, PRICE8, PRICE9, PRICE10, PRICE11, PRICE12, PRICE13) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            statement.setString(1, uuid.toString());
            statement.setString(2, name);
            statement.setLong(3, 1);
            statement.setLong(4, 1);
            statement.setString(5, "BLACK_DESIGN");
            statement.setLong(6, 0);
            statement.setLong(7, 30);
            statement.setLong(8, 360);
            statement.setLong(9, 690);
            statement.setLong(10, 920);
            statement.setLong(11, 1250);
            statement.setLong(12, 1580);
            statement.setLong(13, 1910);
            statement.setLong(14, 2240);
            statement.setLong(15, 2570);
            statement.setLong(16, 2900);
            statement.setLong(17, 3230);
            statement.setLong(18, 3560);
            statement.setLong(19, 3890);
            statement.setLong(20, 4220);
            databaseConnector.update(statement);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean playerExists(UUID uuid, String name) {
        try {
            PreparedStatement statement = databaseConnector.prepareStatement("SELECT UUID, NAME FROM cookieclicker_players WHERE UUID= ?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                if (!(name.equalsIgnoreCase(resultSet.getString("NAME")))) {
                    setValue(uuid, "NAME", name);
                }
                return resultSet.getString("UUID") != null;
            }
            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public void loadPlayerData(UUID uuid) {
        PlayerProfile playerProfile = cookieClicker.getPlayerProfiles().get(uuid);
        playerProfile.setCookies((Long) getValue(uuid, "COOKIES"));
        playerProfile.setPerClick((Long) getValue(uuid, "PER_CLICK"));
        playerProfile.setClickerClicks((Long) getValue(uuid, "CLICKER_CLICKS"));
        playerProfile.setDesigns(Designs.valueOf((String) getValue(uuid, "DESIGN")));
        playerProfile.setOldRank(getRank(uuid, "PER_CLICK"));

        for (int number = 0; number < 14; number++) {
            if (number == 0) {
                playerProfile.setPrice(number, (Long) getValue(uuid, "PRICE"));
            } else {
                playerProfile.setPrice(number, (Long) getValue(uuid, "PRICE" + number));
            }
        }
    }

    public void resetProfile(UUID uuid, String name) {
        cookieClicker.getAlltimeUpdater().getCachedData().remove(name);
        cookieClicker.getTimeUpdater().getCachedData().remove(name);
        if (!(cookieClicker.getPlayerProfiles().containsKey(uuid))) {
            setValue(uuid, "COOKIES", 0);
            setValue(uuid, "PER_CLICK", 1);
            setValue(uuid, "DESIGN", Designs.BLACK_DESIGN.name());
            for (int number = 0; number < 14; number++) {
                if (number == 0) {
                    setValue(uuid, "PRICE", 30);
                    continue;
                }
                setValue(uuid, "PRICE" + number, 30 + 330 * number);
            }
            return;
        }
        PlayerProfile playerProfile = cookieClicker.getPlayerProfiles().get(uuid);
        Objects.requireNonNull(Bukkit.getPlayer(uuid)).closeInventory();
        playerProfile.setCookies(0);
        playerProfile.setPerClick(1);
        playerProfile.setDesigns(Designs.BLACK_DESIGN);
        for (int number = 0; number < 14; number++) {
            playerProfile.setPrice(number, 30 + 330 * number);
        }
    }

    public Object getValue(UUID uuid, String field) {
        try {
            PreparedStatement statement = databaseConnector.prepareStatement("SELECT " + field + " FROM cookieclicker_players WHERE UUID= ?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getObject(field);
            }
            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private int getRank(UUID uuid, String field) {
        try {
            PreparedStatement statement = databaseConnector.prepareStatement("SELECT UUID, NAME, RANK() OVER (ORDER BY " + field + " DESC) rank FROM cookieclicker_players");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (!(resultSet.getString("UUID").equalsIgnoreCase(uuid.toString()))) {
                    continue;
                }
                return resultSet.getInt("rank");
            }
            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public int getRank(UUID uuid) {
        long perClick;
        int databaseRank;
        PlayerProfile playerProfile = cookieClicker.getPlayerProfiles().get(uuid);
        if (playerProfile == null) {
            perClick = (long) getValue(uuid, "PER_CLICK");
        } else {
            perClick = playerProfile.getPerClick();
            setValue(uuid, "PER_CLICK", perClick);
        }
        databaseRank = getRank(uuid, "PER_CLICK");

        int addRank = 0;
        for (PlayerProfile playerProfile1 : cookieClicker.getPlayerProfiles().values()) {
            if (uuid == playerProfile1.getUuid()) {
                continue;
            }
            if ((playerProfile1.getOldRank() > databaseRank) && (playerProfile1.getPerClick() > perClick)) {
                addRank--;
                continue;
            }
            if ((playerProfile1.getOldRank() < databaseRank) && (playerProfile1.getPerClick() < perClick)) {
                addRank++;
            }
        }
        return databaseRank + addRank;
    }

    public int getRegisteredPlayerAmount() {
        try {
            PreparedStatement statement = databaseConnector.prepareStatement("SELECT COUNT(UUID) FROM cookieclicker_players");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("COUNT(UUID)");
            }
            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public void setValue(UUID uuid, String field, Object value) {
        try {
            PreparedStatement statement = databaseConnector.prepareStatement("UPDATE cookieclicker_players SET " + field + "= ? WHERE UUID= ?");
            statement.setObject(1, value);
            statement.setString(2, uuid.toString());
            databaseConnector.update(statement);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public LinkedHashMap<String, Long> orderBy(String field, int range) {
        LinkedHashMap<String, Long> results = new LinkedHashMap<>();
        try {
            PreparedStatement statement = databaseConnector.prepareStatement("SELECT UUID," + field + ",NAME FROM cookieclicker_players ORDER BY " + field + " DESC LIMIT " + range);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                results.put(resultSet.getString("NAME"), resultSet.getLong(field));
            }
            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return results;
    }

}
