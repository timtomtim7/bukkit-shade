package blue.sparse.bshade.data.attach;

import org.bukkit.Server;

public abstract class ServerAttachment implements Attachment<Server> {

	protected final Server server;

	public ServerAttachment(Server server) {
		this.server = server;
	}

	@Override
	public Server getAttachedTo() {
		return server;
	}

}
