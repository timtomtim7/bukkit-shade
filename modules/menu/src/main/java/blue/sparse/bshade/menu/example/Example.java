package blue.sparse.bshade.menu.example;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class Example {

//	public static void test() {
//		Menu menu = new Menu("test-menu");
//		menu.setLayout(new String[]{
//				".........",
//				".A.B.C.D.",
//				"........."
//		});
//
//		Response myResponse = (action, player) -> {
//			player.sendMessage("Hello!");
//		};
//		menu.addResponse("MY_RESPONSE", myResponse);
//		menu.getElement('C').setResponse(myResponse);
//		menu.build();
//	}

	public static class Menu {

	}

	public static class Element {
		private ItemStack icon;
		private Response response;

		public Element(ItemStack icon) {
			this.icon = icon;
		}
	}

	public interface ElementShape extends Set<MenuPosition> {

	}

	public static class MenuPosition {
		public final int x;
		public final int y;

		public MenuPosition(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public interface Response {
		void respond(MenuActionType type, Player player);
	}

	public enum MenuActionType {
		LEFT_CLICK,
		SHIFT_LEFT_CLICK,
		RIGHT_CLICK,
		SHIFT_RIGHT_CLICK,
		LEFT_OUT_OF_BOUNDS,
		RIGHT_OUT_OF_BOUNDS,
		DOUBLE_LEFT_CLICK,
		DROP,
		DRAG,
		NUMBER_1,
		NUMBER_2,
		NUMBER_3,
		NUMBER_4,
		NUMBER_5,
		NUMBER_6,
		NUMBER_7,
		NUMBER_8,
		NUMBER_9
	}
}
