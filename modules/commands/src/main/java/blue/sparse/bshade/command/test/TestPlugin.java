package blue.sparse.bshade.command.test;

import blue.sparse.bshade.command.CommandContext;
import blue.sparse.bshade.command.Commands;
import blue.sparse.bshade.command.parameters.filter.ParameterFilters;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TestPlugin extends JavaPlugin {

	@Retention(RetentionPolicy.RUNTIME)
	@interface OnlyBlocks {}

	@Override
	public void onEnable() {
		ParameterFilters.register(OnlyBlocks.class, Material.class, (a, v) -> v.isBlock());
		Commands.registerCommands(this);
	}

	public void test(CommandContext context, Player player, boolean test, @OnlyBlocks Material material) {
		context.sender.sendMessage(String.format("Test! %s %s %s", player, test, material));
	}

//	public class Channels implements CommandGroup {
//
//		public void join(CommandContext context, String channelName) {
//			context.sender.sendMessage("Joining channel " + channelName);
//		}
//
//		public void leave(CommandContext context, String channelName) {
//			context.sender.sendMessage("Leaving channel " + channelName);
//		}
//
//		public void delete(CommandContext context, String channelName) {
//			context.sender.sendMessage("Deleting channel " + channelName);
//		}
//
//		public void help(CommandContext context, boolean test) {
//			context.sender.sendMessage("HELP! "+test);
//		}
//
//		public class Create implements CommandGroup {
//
//			@Commands.Name("public")
//			public void publicChannel(CommandContext context, String channelName) {
//				context.sender.sendMessage("Creating public channel " + channelName);
//			}
//
//			@Commands.Aliases({"protected", "private"})
//			public void password(CommandContext context, String channelName, String password) {
//				context.sender.sendMessage("Creating private channel " + channelName + ", password " + password);
//			}
//
//			public void test(CommandContext context, Player player, boolean test, Material material) {
//				context.sender.sendMessage(String.format("Test! %s %s %s", player, test, material));
//			}
//
//		}
//
//	}
}
