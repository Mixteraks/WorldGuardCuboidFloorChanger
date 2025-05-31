package me.floor_changer.world_guard_floor_changer.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.List;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.floor_changer.world_guard_floor_changer.WorldGuardFloorChanger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class changeFloor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
//      Check if sender is not a player
        if(!(sender instanceof Player)){
            sender.sendMessage("Only player can use that command!");
            return  true;
        }

//        Sender is a player

        final Player player = (Player) sender;
        String replaceBlock = "";
        if(args.length != 0){
            replaceBlock = args[0];
        }
        List<String> aviableBlocks = WorldGuardFloorChanger.getInstance().getFloorBlocks();
        final ProtectedCuboidRegion cuboid = getCuboidRegionUnderPlayer(player);

        if(cuboid != null){
            //Stand in cuboid
            if(isPlayerOwnerOfRegion(player, cuboid)){
                //Jeżeli cuboid w jakim stoi gracz jest jego właścicielem
                player.sendMessage("§aTa działka należy do ciebie!");

                if(!findItemOnList(replaceBlock, aviableBlocks)){
                    //Nie znalazło bloku do zmiany podłoża

                    player.sendMessage("§a§lNie znaleziono itemu '"+ replaceBlock +"'\nDostępne bloki do zamiany podłoża");
                    for (int i = 0; i < aviableBlocks.size(); i++) {
                        player.sendMessage("§a§l "+ i + ". minecraft:" + aviableBlocks.get(i));
                    }
                } else {
                    // Znalazło blok do zmiany podłoża
                    player.sendMessage("Zmieniono podłoge na blok minecraft:"+replaceBlock);
                }

            } else{
                //Jeżeli cuboid w jakim stoi gracz nie jest jego właścicielem
                player.sendMessage("§cTa działka nie należy do ciebie!");
            }
        }
        else{
            //doesn't stand in cuboid
            player.sendMessage("§cMusisz stać na działce aby ją zmienić!");
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

    public boolean findItemOnList(String item, List<String> list){
        String szukany = item.toLowerCase();
        for (int i = 0; i < list.size(); i++) {
            String aktualny = list.get(i).toLowerCase();

            // Tu porównujemy zawartość (metodą equals), nie referencje
            boolean takiSam = aktualny.equals(szukany);
            WorldGuardFloorChanger.getInstance().getLogger().info(aktualny + ", " + szukany + " | " + takiSam
            );

            if (takiSam) {
                return true;
            }
        }
        return false;
    }

    //Sprawdza czy dany gracz jest właścicielem danego cuboid
    public static boolean isPlayerOwnerOfRegion(Player player, ProtectedRegion region) {
        if (region == null) return false;
        LocalPlayer local = WorldGuardPlugin.inst().wrapPlayer(player);
        return region.isOwner(local);
    }
}
