package blue.sparse.bshade.versions.api;

import blue.sparse.bshade.versions.holograms.Hologram;
import org.bukkit.entity.Player;

public abstract class VersionedHologram {

    protected final Hologram hologram;

    public VersionedHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public abstract void update();
    public abstract void delete();

    public abstract void spawnForPlayer(Player player);
    public abstract void destroyForPlayer(Player player);
}
