package blue.sparse.bshade.data.attach;

import org.bukkit.entity.Player;

public abstract class PlayerAttachment implements Attachment<Player> {

	protected final Player player;

	public PlayerAttachment(Player player) {
		this.player = player;
	}

	@Override
	public Player getAttachedTo() {
		return player;
	}

	public void onTick() {}
}
