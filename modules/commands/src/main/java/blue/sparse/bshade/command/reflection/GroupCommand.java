package blue.sparse.bshade.command.reflection;

import blue.sparse.bshade.command.CommandGroup;
import blue.sparse.bshade.command.Commands;
import blue.sparse.bshade.command.messages.CommandConfig;
import blue.sparse.bshade.command.util.ParsingIterator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupCommand extends CommandPart {

	public final CommandGroup group;
	public final List<CommandPart> subCommands;
	public CommandPart defaultSubCommand;

	protected GroupCommand(
			List<String> names,
			CommandGroup group,
			GroupCommand parent,
			List<CommandPart> subCommands,
			CommandPart defaultSubCommand
	) {
		super(names, parent);
		this.group = group;
		this.subCommands = subCommands;
		this.defaultSubCommand = defaultSubCommand;
		this.isDefault = group.getClass().getAnnotation(Commands.Default.class) != null;
	}

	@Override
	public void execute(
			Command command,
			CommandConfig config,
			CommandSender sender,
			String usedAlias,
			ParsingIterator input
	) {

		final int indexBeforeName = input.getIndex();
		final String name = input.takeUntil(Character::isWhitespace).toLowerCase();
		input.takeWhile(Character::isWhitespace);

		final List<CommandPart> matching;
		if (name.isEmpty()) {
			matching = new ArrayList<>();
		} else {
			matching = subCommands.stream().filter(
					it -> it.names.stream().map(String::toLowerCase).anyMatch(n -> n.startsWith(name.toLowerCase()))
			).collect(Collectors.toList());
		}

		if (matching.isEmpty() && defaultSubCommand != null) {
			input.setIndex(indexBeforeName);
			defaultSubCommand.execute(command, config, sender, usedAlias, input);
		} else if (matching.size() == 1) {
			final CommandPart exact = matching.get(0);
			exact.execute(command, config, sender, usedAlias, input);
		} else if (matching.size() > 1) {
			final GroupCommand matchingGroup = matching.stream()
					.filter(it -> it instanceof GroupCommand)
					.map(it -> ((GroupCommand) it))
					.findFirst()
					.orElse(null);

			matching.remove(matchingGroup);
			for (CommandPart match : matching) {
				final SingleCommand single = (SingleCommand) match;
				final int indexBeforeParse = input.getIndex();
				final ParseResult result = single.parse(command, sender, usedAlias, input);

				if (result instanceof ParseResult.Success) {
					single.executePreParsed(((ParseResult.Success) result).args);
					return;
				}

				input.setIndex(indexBeforeParse);
			}

			if (matchingGroup != null) {
				matchingGroup.execute(command, config, sender, usedAlias, input);
				return;
			}

			input.setIndex(indexBeforeName);

			config.sendInvalidSubCommand(this, sender);
//			sender.sendMessage("subcommand help");

			// Show generated (configurable) help list (possible with multi-page support).
		} else {
			input.setIndex(indexBeforeName);

			// Wrong name and no defaults.
			// Show generated (configurable) help list (possible with multi-page support).
//			sender.sendMessage("subcommand help");
			config.sendInvalidSubCommand(this, sender);
		}

	}

	@Override
	public List<String> tabComplete(
			Command command,
			CommandSender sender,
			String usedAlias,
			ParsingIterator input,
			int parameterStartIndex
	) {
		final List<String> result = new ArrayList<>();
		final int indexBeforeName = input.getIndex();
		final String name = input.takeUntil(Character::isWhitespace).toLowerCase();
		input.takeWhile(Character::isWhitespace);

		final List<CommandPart> matching = subCommands.stream().filter(
				it -> it.names.contains(name)
		).collect(Collectors.toList());

		if (matching.isEmpty()) {
			if (indexBeforeName == parameterStartIndex) {
				final List<String> subCommandCompletions = subCommands.stream()
//						.flatMap(it -> it.names.stream()) // For tab-completing all aliases
						.flatMap(it -> Stream.of(it.names.get(0))) // For tab-completing only primary name
						.filter(it -> it.toLowerCase().startsWith(name))
						.collect(Collectors.toList());

				result.addAll(subCommandCompletions);
//				if (!subCommandCompletions.isEmpty())
//					return subCommandCompletions;
			}
			if (defaultSubCommand != null) {
				input.setIndex(indexBeforeName);
				result.addAll(defaultSubCommand.tabComplete(command, sender, usedAlias, input, parameterStartIndex));
			}
		} else if (matching.size() == 1) {
			final CommandPart exact = matching.get(0);
			result.addAll(exact.tabComplete(command, sender, usedAlias, input, parameterStartIndex));
		} else {
			final GroupCommand matchingGroup = matching.stream()
					.filter(it -> it instanceof GroupCommand)
					.map(it -> ((GroupCommand) it))
					.findFirst()
					.orElse(null);

			matching.remove(matchingGroup);

			for (CommandPart match : matching) {
				final SingleCommand single = (SingleCommand) match;
				final int indexBeforeParse = input.getIndex();
				result.addAll(single.tabComplete(command, sender, usedAlias, input, parameterStartIndex));
				input.setIndex(indexBeforeParse);
			}

			if (matchingGroup != null) {
				result.addAll(matchingGroup.tabComplete(command, sender, usedAlias, input, parameterStartIndex));
			}
		}


		return result;
	}
}