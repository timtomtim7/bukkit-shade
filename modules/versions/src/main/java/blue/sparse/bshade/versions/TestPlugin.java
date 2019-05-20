package blue.sparse.bshade.versions;

import blue.sparse.bshade.versions.api.VersionedBlock;
import blue.sparse.bshade.versions.api.VersionedPlayer;
import blue.sparse.bshade.versions.holograms.Hologram;
import blue.sparse.bshade.versions.holograms.HologramLineScrolling;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class TestPlugin extends JavaPlugin {

    private Hologram hologram;
    private int count = 0;

    String msg = "";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if(hologram == null) {
            hologram = new Hologram(player.getLocation(), new HologramLineScrolling(
                    2,
                    50,
                    ChatColor.translateAlternateColorCodes('&', "&b&lTom1024 &7was born on &7&lOctober 39th, 2075. ")
            ));
            return true;
        }

        hologram.setLine(0, msg);

        return true;

//        VersionedPlayer vPlayer = Versioned.getPlayer(player);
//        vPlayer.sendTitle("This is a test!", ":)", 20, 5, 20);
//        vPlayer.sendActionBar(ChatColor.translateAlternateColorCodes('&', "&cc&aa&bb&11"));
//
//        Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);
//        VersionedBlock vBlock = Versioned.getBlock(targetBlock);
//
//        vBlock.getDrops(player.getItemInHand()).forEach(item -> player.getInventory().addItem(item));
//        vBlock.setCrackedAmount(.9f);
//
//        player.getWorld().getLivingEntities().stream()
//                .filter(entity -> entity.getLocation().distanceSquared(player.getLocation()) < 10.0 * 10.0)
//                .forEach(
//                        livingEntity -> {
//                            List<ItemStack> drops = Versioned.getEntity(livingEntity).getDrops(3);
//                            for (ItemStack drop : drops) {
//                                player.getInventory().addItem(drop);
//                            }
//                        }
//                );
//
//
//        if(hologram == null) {
//            hologram = new Hologram(player.getLocation(), "Testing, my friend.");
//        } else {
//            hologram.setLocation(player.getLocation());
//            int rand = ThreadLocalRandom.current().nextInt(0, 16);
//            String colorCode = Integer.toString(rand, 16);
//
//            hologram.addLine(ChatColor.translateAlternateColorCodes('&', "&" + colorCode + "Once again."));
//            if(count % 3 == 0)
//                hologram.setVisible(false);
//            else
//                hologram.setVisible(true);
//        }
//
//        count++;
//        return true;
    }
}
