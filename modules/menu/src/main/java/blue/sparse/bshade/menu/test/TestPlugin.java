package blue.sparse.bshade.menu.test;

import blue.sparse.bshade.menu.Menu;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

	@Override
	public void onEnable() {

	}

	public static class TestMenu extends Menu {
		public TestMenu() {
			super("test");
			setLayout(
					"A|.......",
					"B|.......",
					"C|......."
			);
		}
	}
}
