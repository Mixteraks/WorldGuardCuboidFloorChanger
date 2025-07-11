package me.floor_changer.world_guard_floor_changer.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class cuboid_Info implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
//      Check if sender is not a player
        if(!(sender instanceof Player)){
            sender.sendMessage("Only player can use that command!");
            return  true;
        }

//        Sender is a player

        final Player player = (Player) sender;
        final ProtectedCuboidRegion cuboid = getCuboidRegionUnderPlayer(player);

        if(cuboid != null){
            //Stand in cuboid
            player.sendMessage("§aCuboid Information:");
            if(isPlayerOwnerOfRegion(player, cuboid)){
                //Jeżeli cuboid w jakim stoi gracz jest jego właścicielem
                player.sendMessage("§aTa działka należy do ciebie!");
            } else{
                //Jeżeli cuboid w jakim stoi gracz nie jest jego właścicielem
                player.sendMessage("§cTa działka nie należy do ciebie!");
            }
        }
        else{
            //doesn't stand in cuboid
            player.sendMessage("§cMusisz stać na działce!");
        }

        return true;
    }

    //Sprawdza cuboid na jakim jest gracz i go zwraca
    public static ProtectedCuboidRegion getCuboidRegionUnderPlayer(Player player) {
        Location loc = player.getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager manager = container.get(BukkitAdapter.adapt(loc.getWorld()));
        if (manager == null) return null;

        ApplicableRegionSet set = manager.getApplicableRegions(BukkitAdapter.asBlockVector(loc));

        for (ProtectedRegion region : set) {
            if (region instanceof ProtectedCuboidRegion cuboid) {
                return cuboid;
            }
        }
        return null;
    }

    //Sprawdza czy dany gracz jest właścicielem danego cuboid
    public static boolean isPlayerOwnerOfRegion(Player player, ProtectedRegion region) {
        if (region == null) return false;
        LocalPlayer local = WorldGuardPlugin.inst().wrapPlayer(player);
        return region.isOwner(local);
    }
}
