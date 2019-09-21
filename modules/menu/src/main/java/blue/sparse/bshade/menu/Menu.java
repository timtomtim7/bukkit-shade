package blue.sparse.bshade.menu;

import blue.sparse.bshade.menu.element.ElementContainer;
import org.bukkit.inventory.Inventory;

public class Menu extends ElementContainer {

	private Inventory inventory;

	public Menu(String id) {}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void setLayout(Layout layout) {
		if(layout.getWidth() != 9)
			throw new IllegalArgumentException("Menu layout must have a width of 9.");
		super.setLayout(layout);
	}
}
