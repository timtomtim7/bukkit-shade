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

    private static final ChatColor[] colors = Arrays.stream(ChatColor.values()).filter(ChatColor::isColor).filter(it -> it != ChatColor.BLACK && it != ChatColor.WHITE && it != ChatColor.GRAY && it != ChatColor.DARK_GRAY).toArray(ChatColor[]::new);
    private static final ChatColor[] modifiers = Arrays.stream(ChatColor.values()).filter(ChatColor::isFormat).toArray(ChatColor[]::new);

    private Hologram hologram;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        for (int i = 0; i < 1000; i++) {
            player.sendMessage("");
        }

        debugUpdate(player, "&aHologram Test - You should see a scrolling hologram with generated colors and modifiers.");

        if (hologram == null) {
            hologram = new Hologram(player.getLocation(), new HologramLineScrolling(2, 50, i -> {
                final ChatColor color = colors[ThreadLocalRandom.current().nextInt(colors.length)];
                final ChatColor modifier = modifiers[ThreadLocalRandom.current().nextInt(modifiers.length)];
                return String.format("%s%sCOLORS\u00a7r ", color, modifier);
            }));
        }

        debugUpdate(player, "&bPlayer Test - You should see a title message and a colorful action bar message.");

        VersionedPlayer vPlayer = Versioned.getPlayer(player);
        vPlayer.sendTitle("This is a test!", ":)", 20, 5, 20);
        vPlayer.sendActionBar(ChatColor.translateAlternateColorCodes('&', "&cc&aa&bb&11"));

        debugUpdate(player, "&dBlock Test - Your target block should be cracked, and it's drops relative to the item you are holding should be in your inventory.");

        VersionedBlock vBlock = Versioned.getBlock(player.getTargetBlock((Set<Material>) null, 5));
        vBlock.getDrops(player.getItemInHand()).forEach(item -> player.getInventory().addItem(item));
        vBlock.setCrackedAmount(.9f);

        debugUpdate(player, "&eEntity Test - You should have received the drops of entities within a 10 block radius as if you killed them with looting 3.");

        player.getWorld().getLivingEntities().stream().filter(entity -> entity.getLocation().distanceSquared(player.getLocation()) < 10.0 * 10.0).forEach(livingEntity -> Versioned.getEntity(livingEntity).getDrops(3).forEach(drop -> player.getInventory().addItem(drop)));

        return true;
    }

    private void debugUpdate(Player player, String string) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', string));
        player.sendMessage("");
    }
}
