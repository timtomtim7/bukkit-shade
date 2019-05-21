package blue.sparse.bshade.versions.v1_13_R1;

import blue.sparse.bshade.versions.api.VersionedBlock;
import net.minecraft.server.v1_13_R1.*;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_13_R1.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class VersionedBlockImpl extends VersionedBlock {

    public VersionedBlockImpl(org.bukkit.block.Block block) {
        super(block);
    }

    private Block getNmsBlock() {
        return CraftMagicNumbers.getBlock(this.block.getType());
    }

    private BlockPosition getNmsBlockPosition() {
        return new BlockPosition(block.getX(), block.getY(), block.getZ());
    }

    private World getNmsBlockAccess() {
        CraftWorld craftWorld = (CraftWorld) block.getWorld();
        return craftWorld.getHandle();
    }

    @Override
    public void setCrackedAmount(float amount) {
        BlockPosition blockPosition = getNmsBlockPosition();
        PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(
                blockPosition.hashCode() * -13,
                blockPosition,
                (int) (amount < 0f || amount > 1f ? -1 : amount * 7f)
        );

        Collection<Entity> entities = block.getWorld().getNearbyEntities(block.getLocation(), 20, 20, 20);
        for(Entity entity : entities) {
            if(!(entity instanceof CraftPlayer))
                continue;

            CraftPlayer player = (CraftPlayer) entity;
            player.getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public int getBreakTicks() {
        return (int) (1f/getNmsBlock().getBlockData().e(getNmsBlockAccess(), getNmsBlockPosition()));
    }


    @Override
    public List<ItemStack> getDrops(ItemStack tool) {
        List<ItemStack> items = new ArrayList<>();
        Block nmsBlock = getNmsBlock();
        IBlockData blockData = nmsBlock.getBlockData();
        BlockPosition blockPosition = getNmsBlockPosition();
        CraftWorld craftWorld = (CraftWorld) block.getWorld();
        WorldServer nmsWorld = craftWorld.getHandle();

        if (tool.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0) {
            net.minecraft.server.v1_13_R1.ItemStack item = nmsBlock.a(nmsWorld, blockPosition, blockData);
            ItemStack craftItemStack = CraftItemStack.asBukkitCopy(item);
            items.add(craftItemStack);
            return items;
        }

        items.addAll(block.getDrops(tool));

        int count = nmsBlock.getDropCount(
                blockData,
                tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS),
                getNmsBlockAccess(),
                blockPosition,
                ThreadLocalRandom.current()
        );

        items.forEach(item -> item.setAmount(count));
        return items;
    }
}
