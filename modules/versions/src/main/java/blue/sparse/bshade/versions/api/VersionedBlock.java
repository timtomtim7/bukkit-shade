package blue.sparse.bshade.versions.api;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class VersionedBlock {
    protected final Block block;

    public VersionedBlock(Block block) {
        this.block = block;
    }

    public abstract void setCrackedAmount(float amount);

    public abstract int getBreakTicks();

    public List<ItemStack> getDrops(int fortune, boolean silkTouch) {
        ItemStack tool = new ItemStack(Material.DIAMOND_PICKAXE);
        tool.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, fortune);
        if (silkTouch)
            tool.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        return getDrops(tool);
    }

    public abstract List<ItemStack> getDrops(ItemStack tool);
}
