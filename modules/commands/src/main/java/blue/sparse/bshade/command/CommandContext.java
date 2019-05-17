package blue.sparse.bshade.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandContext<T extends CommandSender> {

	public final Command command;
	public final T sender;
	public final String usedAlias;
	public final String trailing;

	public CommandContext(
			Command command,
			T sender,
			String usedAlias,
			String trailing
	) {
		this.command = command;
		this.sender = sender;
		this.usedAlias = usedAlias;
		this.trailing = trailing;
	}

}