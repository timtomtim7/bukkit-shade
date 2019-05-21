package blue.sparse.bshade.versions.v1_11_R1;

import blue.sparse.bshade.versions.api.VersionedEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VersionedEntityImpl extends VersionedEntity {


    public VersionedEntityImpl(Entity entity) {
        super(entity);
    }

    private net.minecraft.server.v1_11_R1.Entity getNmsEntity() {
        return ((CraftEntity) entity).getHandle();
    }

    @Override
    public List<ItemStack> getDrops(int looting, boolean forceLooting) {

        List<ItemStack> items = new ArrayList<>();

        net.minecraft.server.v1_11_R1.Entity nmsEntity = getNmsEntity();
        if (!(nmsEntity instanceof EntityLiving))
            return items;

        EntityLiving livingNmsEntity = (EntityLiving) nmsEntity;
        Field drops;

        try {
            DamageSource lastDamageSource = DamageSource.GENERIC;

            drops = EntityLiving.class.getDeclaredField("drops");
            drops.setAccessible(true);
            drops.set(livingNmsEntity, items);

            Method addDropsMethod = EntityLiving.class.getDeclaredMethod("a", boolean.class, int.class, DamageSource.class);
            addDropsMethod.setAccessible(true);

            if(forceLooting) {
                EntityHuman human = new EntityHuman(nmsEntity.getWorld(), new GameProfile(UUID.randomUUID(), "fake")) {
                    public boolean isSpectator() {
                        return false;
                    }
                    public boolean z() {
                        return false;
                    }
                };

                ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
                item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, looting);
                human.setSlot(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(item));

                lastDamageSource = DamageSource.playerAttack(human);
            } else {
                Field combatEntryListField = CombatTracker.class.getDeclaredField("a");
                combatEntryListField.setAccessible(true);

                CombatTracker combatTracker = livingNmsEntity.getCombatTracker();
                List<?> combatEntryList = (List<?>) combatEntryListField.get(combatTracker);

                if (!combatEntryList.isEmpty()) {
                    Object lastEntry = combatEntryList.get(combatEntryList.size() - 1);
                    lastDamageSource = ((CombatEntry) lastEntry).a();
                }
            }

            addDropsMethod.invoke(livingNmsEntity, true, looting, lastDamageSource);

            drops.set(livingNmsEntity, new ArrayList<>());
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }


        return items;
    }
}
