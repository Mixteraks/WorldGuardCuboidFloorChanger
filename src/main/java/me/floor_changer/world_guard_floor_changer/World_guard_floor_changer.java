package me.floor_changer.world_guard_floor_changer;

//import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
//import com.sk89q.worldguard.bukkit.BukkitAdapter;
//import com.sk89q.worldguard.protection.managers.RegionManager;
//import com.sk89q.worldguard.protection.managers.RegionContainer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
//import com.sk89q.worldguard.WorldGuard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.managers.RegionManager;

import me.floor_changer.world_guard_floor_changer.commands.changeFloor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import org.bukkit.plugin.java.JavaPlugin;

public final class World_guard_floor_changer extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("World Guard Floor Changer is Active");
        getCommand("changefloor").setExecutor(new changeFloor());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

//    public boolean isRegionExist() {
//        RegionContainerImpl container = WorldGuard.getInstance().getPlatform().getRegionContainer();
//        RegionManager regions = container.get(world);
//        if (regions != null) {
//            return regions.getRegion("spawn");
//        } else {
//            // The world has no region support or region data failed to load
//        }
//        return false;
//    }

    public static boolean isPlayerInRegion(Player player, String regionId) {
        Location loc = player.getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(loc.getWorld()));

        if (regions == null) return false;

        ApplicableRegionSet set = regions.getApplicableRegions(BukkitAdapter.asBlockVector(loc));

        for (ProtectedRegion region : set) {
                return true;
//            if (region.getId().equalsIgnoreCase(regionId)) {
//            }
        }
        return false;
    }
}
