package blue.sparse.bshade.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ItemStackUtil {

	private ItemStackUtil() {
	}

	public static List<Item> dropAll(List<ItemStack> items, Location location, boolean merge, boolean naturally) {
		if(merge)
			items = new StackingItemList(items);

		final List<Item> result = new ArrayList<>();

		final World world = location.getWorld();
		for (ItemStack item : items) {
			if (naturally) {
				result.add(world.dropItemNaturally(location, item));
			} else {
				result.add(world.dropItem(location, item));
			}
		}

		return result;
	}

	public static List<ItemStack> mergeStacks(List<ItemStack> input, boolean sorted) {
		final List<ItemStack> result = new StackingItemList(input);

		if (sorted)
			result.sort(Comparator.comparing(ItemStack::getType));

		return result;
	}

}
