package blue.sparse.bshade.util.test;

import blue.sparse.bshade.util.StackingItemList;
import blue.sparse.bshade.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		final List<ItemStack> items = new StackingItemList();

		final ThreadLocalRandom rand = ThreadLocalRandom.current();
		int countWeird = 0;
		for(int i = 0; i < 148; i++) {
			items.add(new ItemStack(Material.ROTTEN_FLESH, rand.nextInt(1, 5)));
			items.add(new ItemStack(Material.EMERALD, rand.nextInt(1, 5)));
			items.add(new ItemStack(Material.ENDER_PEARL, rand.nextInt(1, 5)));


			ItemStack weird = new ItemStack(Material.DIRT);
			weird.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
			final ItemMeta meta = weird.getItemMeta();
			meta.setDisplayName(StringUtil.color("&aTest!"));
			meta.setLore(Collections.singletonList(StringUtil.color("&bLore!")));
			weird.setItemMeta(meta);

			final int weirdAmount = rand.nextInt(1, 5);
			weird.setAmount(weirdAmount);
			countWeird += weirdAmount;
			items.add(weird);
		}

		System.out.println(items);
		System.out.println(countWeird);

//		final List<ItemStack> newItems = ItemStackUtil.mergeStacks(items, true);
//		System.out.println(newItems);
	}

}
