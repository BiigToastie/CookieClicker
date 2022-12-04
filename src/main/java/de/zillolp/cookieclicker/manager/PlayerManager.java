package de.zillolp.cookieclicker.manager;

import de.zillolp.cookieclicker.CookieClicker;
import de.zillolp.cookieclicker.config.LanguageTools;
import de.zillolp.cookieclicker.utils.HologramUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerManager {
    private final CookieClicker cookieClicker = CookieClicker.cookieClicker;
    private final DatabaseManager databaseManager;
    private final Player player;
    private final UUID uuid;
    private final String name;
    private final HashMap<String, Object> values;
    private final HashMap<Location, HologramUtil> holograms;

    public PlayerManager(Player player) {
        databaseManager = cookieClicker.getDatabaseManager();
        this.player = player;
        uuid = player.getUniqueId();
        name = player.getName();
        values = new HashMap<>();
        holograms = new HashMap<>();
    }

    public HashMap<String, Object> getValues() {
        return values;
    }

    public void loadProfile() {
        CompletableFuture.runAsync(() -> {
            if (!(databaseManager.playerExists(uuid, name))) {
                databaseManager.createPlayer(uuid, name);
            }
            Bukkit.getScheduler().runTaskLaterAsynchronously(cookieClicker, () -> databaseManager.loadPlayerData(uuid), 4);
        });
    }

    public void spawnHolograms() {
        for (Map.Entry<String, Location> clicker : cookieClicker.getClickerLocations().entrySet()) {
            Location location = clicker.getValue();
            HologramUtil hologramUtil = new HologramUtil(player, LanguageTools.getLines("Clickerhologram"), location);
            hologramUtil.spawn();
            holograms.put(location, hologramUtil);
        }
    }

    public void deleteHolograms() {
        for (Map.Entry<Location, HologramUtil> hologramUtil : holograms.entrySet()) {
            hologramUtil.getValue().delete();
        }
        holograms.clear();
    }

    public void reloadHolograms() {
        deleteHolograms();
        spawnHolograms();
    }

    public void uploadStats() {
        for (Map.Entry<String, Object> value : values.entrySet()) {
            databaseManager.setValue(uuid, value.getKey(), value.getValue());
        }
    }
}