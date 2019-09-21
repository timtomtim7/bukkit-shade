package blue.sparse.bshade.i18n.test;

import blue.sparse.bshade.i18n.LocaleConfig;
import blue.sparse.bshade.i18n.placeholder.PlaceholderStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ThreadLocalRandom;

public final class TestPlugin extends JavaPlugin {

	private int test;

	@Override
	public void onEnable() {
		LocaleConfig.setDefaults(TestLocale.class);
		PlaceholderStack global = PlaceholderStack.global();
		global.add("someGlobal", 18);
		global.add(null, this);

		this.test = 42;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player))
			return true;

		this.test = ThreadLocalRandom.current().nextInt(10);

		Player player = (Player) sender;

		PlaceholderStack stack = new PlaceholderStack();
		stack.add("player", player);

		int count = 5;
		double price = 53.0;

		stack.addMap("count", count, "price", price);
		TestLocale.MESSAGE_BUY.send(sender, stack);

		stack.push("test", 5);
		// whatever
		stack.pop();
		stack.push("test", 6);
		// whatever part 2
		stack.pop();

		stack.push("weird", "thing");
		// Here, "weird" -> "thing" is available until `pop` is called.
		TestLocale.MESSAGE_TEST.send(sender, stack);
		stack.pop();



		/*
			message:
		  	  buy: "{player.name} purchased {count}x for ${price #,##0.00}. {someGlobal} {test}"
		 */
		/*
			Tom1024 purchased 5x for $53.00. 18 4
		 */

		return true;
	}
}
