package blue.sparse.bshade.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

public enum Action {

	LEFT_CLICK,
	SHIFT_LEFT_CLICK,
	RIGHT_CLICK,
	SHIFT_RIGHT_CLICK,
	MIDDLE_CLICK,
	LEFT_OUT_OF_BOUNDS,
	RIGHT_OUT_OF_BOUNDS,
	DOUBLE_LEFT_CLICK,
	DROP,
	CONTROL_DROP,
	NUMBER_1,
	NUMBER_2,
	NUMBER_3,
	NUMBER_4,
	NUMBER_5,
	NUMBER_6,
	NUMBER_7,
	NUMBER_8,
	NUMBER_9;

	public static Action get(InventoryInteractEvent event) {
		if (event instanceof InventoryClickEvent) {
			InventoryClickEvent click = (InventoryClickEvent) event;
			switch (click.getClick()) {
				case LEFT:
					return LEFT_CLICK;
				case SHIFT_LEFT:
					return SHIFT_LEFT_CLICK;
				case RIGHT:
					return RIGHT_CLICK;
				case SHIFT_RIGHT:
					return SHIFT_RIGHT_CLICK;
				case WINDOW_BORDER_LEFT:
					return LEFT_OUT_OF_BOUNDS;
				case WINDOW_BORDER_RIGHT:
					return RIGHT_OUT_OF_BOUNDS;
				case MIDDLE:
					return MIDDLE_CLICK;
				case NUMBER_KEY:
					switch (click.getHotbarButton()) {
						case 1:
							return NUMBER_1;
						case 2:
							return NUMBER_2;
						case 3:
							return NUMBER_3;
						case 4:
							return NUMBER_4;
						case 5:
							return NUMBER_5;
						case 6:
							return NUMBER_6;
						case 7:
							return NUMBER_7;
						case 8:
							return NUMBER_8;
						case 9:
							return NUMBER_9;
					}
					return null;
				case DOUBLE_CLICK:
					return DOUBLE_LEFT_CLICK;
				case DROP:
					return DROP;
				case CONTROL_DROP:
					return CONTROL_DROP;
			}
		}
		return null;
	}
}
