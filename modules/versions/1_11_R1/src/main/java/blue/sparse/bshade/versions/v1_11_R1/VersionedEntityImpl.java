package blue.sparse.bshade.versions.v1_11_R1;

import blue.sparse.bshade.versions.api.VersionedEntity;
import net.minecraft.server.v1_11_R1.EntityLiving;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class VersionedEntityImpl extends VersionedEntity {

    public VersionedEntityImpl(Entity entity) {
        super(entity);
    }

    private net.minecraft.server.v1_11_R1.Entity getNmsEntity() {
        return ((CraftEntity) entity).getHandle();
    }

    @Override
    public List<ItemStack> getDrops(int looting) {

        List<ItemStack> items = new ArrayList<>();
        if (!(getNmsEntity() instanceof EntityLiving))
            return items;

        EntityLiving nmsEntity = (EntityLiving) getNmsEntity();
        Field drops;

        try {
            drops = nmsEntity.getClass().getField("drops");
            drops.setAccessible(true);
            drops.set(nmsEntity, items);

            Method dropDeathLoot = nmsEntity.getClass().getDeclaredMethod("dropDeathLoot", boolean.class, int.class);
            dropDeathLoot.setAccessible(true);
            dropDeathLoot.invoke(nmsEntity, true, looting);

            Method dropEquipment = nmsEntity.getClass().getDeclaredMethod("dropEquipment", boolean.class, int.class);
            dropEquipment.setAccessible(true);
            dropEquipment.invoke(nmsEntity, true, looting);


            drops.set(nmsEntity, new ArrayList<>());
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }


        return items;
    }
}
