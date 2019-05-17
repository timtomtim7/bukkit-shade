package blue.sparse.bshade.command.reflection;

import blue.sparse.bshade.command.messages.CommandConfig;
import blue.sparse.bshade.command.util.ParsingIterator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class CommandPart {

	public final List<String> names;
	public final GroupCommand parent;
	protected boolean isDefault;

	protected CommandPart(List<String> names, GroupCommand parent) {
		this.names = names;
		this.parent = parent;
	}

	public String getSignaturePrefix() {
		final StringBuilder result = new StringBuilder();
		buildSignaturePrefix(result);
		return result.toString();
	}

	protected void buildSignaturePrefix(StringBuilder builder) {
		if (parent != null) {
			parent.buildSignaturePrefix(builder);
		}

		if (builder.length() == 0) {
			builder.append("/");
		} else {
			builder.append(" ");
		}

		builder.append(names.get(0));
	}

	public boolean isDefault() {
		return isDefault;
	}

	public abstract void execute(
			Command command,
			CommandConfig config,
			CommandSender sender,
			String usedAlias,
			ParsingIterator input
	);

	public abstract List<String> tabComplete(
			Command command,
			CommandSender sender,
			String usedAlias,
			ParsingIterator input,
			int parameterStartIndex
	);

}