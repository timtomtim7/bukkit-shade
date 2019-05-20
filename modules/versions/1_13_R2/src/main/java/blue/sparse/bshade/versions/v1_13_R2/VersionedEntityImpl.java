package blue.sparse.bshade.versions.v1_13_R2;

import blue.sparse.bshade.versions.api.VersionedEntity;
import net.minecraft.server.v1_13_R2.CombatEntry;
import net.minecraft.server.v1_13_R2.CombatTracker;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.EntityLiving;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
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

    private net.minecraft.server.v1_13_R2.Entity getNmsEntity() {
        return ((CraftEntity) entity).getHandle();
    }

    @Override
    public List<ItemStack> getDrops(int looting) {

        List<ItemStack> items = new ArrayList<>();

        net.minecraft.server.v1_13_R2.Entity nmsEntity = getNmsEntity();
        if (!(nmsEntity instanceof EntityLiving))
            return items;

        EntityLiving livingNmsEntity = (EntityLiving) nmsEntity;
        Field drops;

        try {
            drops = EntityLiving.class.getDeclaredField("drops");
            drops.setAccessible(true);
            drops.set(livingNmsEntity, items);

            Method addDropsMethod = EntityLiving.class.getDeclaredMethod("a", boolean.class, int.class, DamageSource.class);
            addDropsMethod.setAccessible(true);

            DamageSource lastDamageSource = DamageSource.GENERIC;

            CombatTracker combatTracker = livingNmsEntity.getCombatTracker();
            Field combatEntryListField = CombatTracker.class.getDeclaredField("a");
            combatEntryListField.setAccessible(true);

            List<?> combatEntryList = (List<?>) combatEntryListField.get(combatTracker);

            if (!combatEntryList.isEmpty()) {
                Object lastEntry = combatEntryList.get(combatEntryList.size() - 1);
                lastDamageSource = ((CombatEntry) lastEntry).a();
            }

            addDropsMethod.invoke(livingNmsEntity, true, looting, lastDamageSource);

            drops.set(livingNmsEntity, new ArrayList<>());
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }


        return items;
    }
}
