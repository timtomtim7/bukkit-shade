package blue.sparse.bshade.math.test;

import blue.sparse.bshade.math.blocks.section.BlockSection;
import blue.sparse.bshade.math.blocks.section.CuboidSection;
import blue.sparse.bshade.math.blocks.section.EllipticCylinderSection;
import blue.sparse.bshade.math.blocks.util.BlockPosition;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

	@Override
	public void onEnable() {

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;

		BlockPosition origin = new BlockPosition(player.getLocation());
		BlockSection section = new EllipticCylinderSection(player.getWorld(), origin.minus(6), origin.plus(6));

		section.hollow().forEach(it -> it.set(Material.STAINED_GLASS));

		return true;
	}
}
