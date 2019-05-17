package blue.sparse.bshade.util;

import org.bukkit.inventory.ItemStack;

import java.util.*;

public class StackingItemList extends AbstractList<ItemStack> {

	private final List<ItemStack> backingList = new ArrayList<>();

	public StackingItemList() { }

	public StackingItemList(Collection<ItemStack> collection) {
		this.addAll(collection);
	}


	@Override
	public boolean add(ItemStack item) {
		final ItemStack similar = backingList.stream()
				.filter(it -> it.getAmount() < it.getMaxStackSize() && it.isSimilar(item))
				.findFirst()
				.orElse(null);

		if (similar == null) {
			backingList.add(item.clone());
		} else {
			int newAmount = similar.getAmount() + item.getAmount();

			int stackAmount = Math.min(newAmount, similar.getMaxStackSize());
			similar.setAmount(stackAmount);
			newAmount -= stackAmount;

			while (newAmount > 0) {
				stackAmount = Math.min(newAmount, similar.getMaxStackSize());

				final ItemStack clone = similar.clone();
				clone.setAmount(stackAmount);
				backingList.add(clone);

				newAmount -= stackAmount;
			}
		}

		return true;
	}

	@Override
	public ItemStack get(int index) {
		return backingList.get(index);
	}

	@Override
	public int size() {
		return backingList.size();
	}

	@Override
	public Iterator<ItemStack> iterator() {
		return backingList.iterator();
	}
}
