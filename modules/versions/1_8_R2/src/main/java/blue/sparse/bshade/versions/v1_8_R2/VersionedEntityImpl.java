package blue.sparse.bshade.versions.v1_8_R2;

import blue.sparse.bshade.versions.api.VersionedEntity;
import net.minecraft.server.v1_8_R2.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class VersionedEntityImpl extends VersionedEntity {

    public VersionedEntityImpl(Entity entity) {
        super(entity);
    }

    private net.minecraft.server.v1_8_R2.Entity getNmsEntity() {
        return ((CraftEntity) entity).getHandle();
    }

    @Override
    public List<ItemStack> getDrops(int looting, boolean forceLooting) {

        List<ItemStack> items = new ArrayList<>();
        if (!(getNmsEntity() instanceof EntityLiving))
            return items;

        Random random = ThreadLocalRandom.current();
        EntityLiving nmsEntity = (EntityLiving) getNmsEntity();
        Field drops;

        try {
            drops = EntityLiving.class.getDeclaredField("drops");
            drops.setAccessible(true);
            drops.set(nmsEntity, items);

            Method dropDeathLoot = EntityLiving.class.getDeclaredMethod("dropDeathLoot", boolean.class, int.class);
            dropDeathLoot.setAccessible(true);
            dropDeathLoot.invoke(nmsEntity, true, looting);

            Method dropEquipment = EntityLiving.class.getDeclaredMethod("dropEquipment", boolean.class, int.class);
            dropEquipment.setAccessible(true);
            dropEquipment.invoke(nmsEntity, true, looting);

            Method getRareDrop = EntityLiving.class.getDeclaredMethod("getRareDrop");
            getRareDrop.setAccessible(true);

            if(random.nextFloat() < 0.025F + (float) looting * 0.01F)
                getRareDrop.invoke(nmsEntity);

            drops.set(nmsEntity, new ArrayList<>());
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }


        return items;
    }
}
