package blue.sparse.bshade.versions.api;

import org.bukkit.entity.Player;

public abstract class VersionedPlayer {

    protected final Player player;

    public VersionedPlayer(Player player) {
        this.player = player;
    }

    public abstract void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut);
    public abstract void sendActionBar(String message);
}