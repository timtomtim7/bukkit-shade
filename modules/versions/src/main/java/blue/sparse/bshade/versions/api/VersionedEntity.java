package blue.sparse.bshade.versions.api;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class VersionedEntity {
    protected final Entity entity;

    public VersionedEntity(Entity entity) {
        this.entity = entity;
    }

    public abstract List<ItemStack> getDrops(int looting);
}
