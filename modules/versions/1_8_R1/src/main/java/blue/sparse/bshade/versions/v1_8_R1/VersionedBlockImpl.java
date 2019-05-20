package blue.sparse.bshade.versions.v1_8_R1;

import blue.sparse.bshade.versions.api.VersionedBlock;
import net.minecraft.server.v1_8_R1.Block;
import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.PacketPlayOutBlockBreakAnimation;
import net.minecraft.server.v1_8_R1.World;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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

        Collection<Entity> entities = block.getWorld().getLivingEntities().stream()
                .filter(entity -> entity.getLocation().distanceSquared(block.getLocation()) < 20.0*20.0)
                .collect(Collectors.toSet());

        for(Entity entity : entities) {
            if(!(entity instanceof CraftPlayer))
                continue;

            CraftPlayer player = (CraftPlayer) entity;
            player.getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public int getBreakTicks() {
        return (int) (1f/getNmsBlock().g(getNmsBlockAccess(), getNmsBlockPosition()));
    }

    @Override
    public List<ItemStack> getDrops(int fortune, boolean silkTouch) {
        List<ItemStack> items = new ArrayList<>();
        Block nmsBlock = getNmsBlock();

        if (silkTouch) {
            net.minecraft.server.v1_8_R1.ItemStack item = new net.minecraft.server.v1_8_R1.ItemStack(getNmsBlock());
            CraftItemStack craftItemStack = CraftItemStack.asCraftMirror(item);
            items.add(craftItemStack);
            return items;
        }

        items.addAll(block.getDrops());
        int count = nmsBlock.getDropCount(fortune, ThreadLocalRandom.current());
        items.forEach(item -> item.setAmount(count));
        return items;
    }
}
