package me.floor_changer.world_guard_floor_changer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import me.floor_changer.world_guard_floor_changer.commands.changeFloor;
import me.floor_changer.world_guard_floor_changer.commands.cuboid_Info;

public final class WorldGuardFloorChanger extends JavaPlugin {
    private static WorldGuardFloorChanger  instance;

    // Tu trzymamy bloki z JSON-a
    private List<String> floorBlocks;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("World Guard Floor Changer jest aktywny");

        // Rejestracja komend
        getCommand("changefloor").setExecutor(new changeFloor());
        getCommand("cuboid_info").setExecutor(new cuboid_Info());

        // Wczytanie JSON-a
        loadBlocks();
        getLogger().info("Wczytano " + floorBlocks.size() + " bloków.");
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static WorldGuardFloorChanger  getInstance() {
        return instance;
    }

    private void loadBlocks() {
        try (InputStream is = getResource("floor.json")) {
            if (is == null) {
                getLogger().severe("Nie znaleziono floor.json w resources!");
                return;
            }
            Gson gson = new Gson();
            Type type = new TypeToken<Wrapper>(){}.getType();
            Wrapper w = gson.fromJson(new InputStreamReader(is), type);
            if (w == null || w.blocks == null) {
                getLogger().severe("floor.json jest niepoprawny lub pole 'messages' nie istnieje!");
                return;
            }
            this.floorBlocks = w.blocks;
        } catch (Exception e) {
            getLogger().warning("Błąd podczas wczytywania floor.json");
            e.printStackTrace();
        }
    }


    /**
     * Zwraca wczytaną listę bloków.
     */
    public List<String> getFloorBlocks() {
        return this.floorBlocks;
    }

    /**
     * Sprawdza, czy gracz stoi w jakimkolwiek regionie WorldGuard.
     */
    public static boolean isPlayerInAnyRegion(Player player) {
        Location loc = player.getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager manager = container.get(BukkitAdapter.adapt(loc.getWorld()));
        if (manager == null) return false;

        ApplicableRegionSet set = manager.getApplicableRegions(BukkitAdapter.asBlockVector(loc));
        return !set.getRegions().isEmpty();
    }

    // Klasa-wrapping JSON-a
    private static class Wrapper {
        List<String> blocks;
    }
}
