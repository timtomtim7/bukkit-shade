package blue.sparse.bshade.command.reflection;

import blue.sparse.bshade.command.CommandContext;
import blue.sparse.bshade.command.CommandGroup;
import blue.sparse.bshade.command.Commands;
import blue.sparse.bshade.command.messages.CommandConfig;
import blue.sparse.bshade.command.util.ParsingIterator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ReflectionCommandLoader {

	private ReflectionCommandLoader() {
	}

	//	@SuppressWarnings("unchecked")
	public static List<Command> getTopLevelCommands(Object container) throws ReflectiveOperationException {
		final List<CommandPart> commands = new ArrayList<>();

		if(container instanceof CommandGroup) {
			GroupCommand parent = getGroup(null, container.getClass(), ((CommandGroup) container));
			commands.add(parent);
		}else{
			commands.addAll(getGroupCommands(container, null));
			commands.addAll(getSingleCommands(container, null));
		}

//		System.out.println(commands);

		return commands.stream()
				.map(ReflectionCommandLoader::createBukkitCommand)
				.collect(Collectors.toList());
	}

	private static List<GroupCommand> getGroupCommands(Object container, GroupCommand parent) throws ReflectiveOperationException {
		final Class<?> containerClass = container.getClass();
		final Class<?>[] nested = containerClass.getClasses();

		final List<GroupCommand> result = new ArrayList<>();

		for (Class<?> clazz : nested) {
			if (!CommandGroup.class.isAssignableFrom(clazz))
				continue;

			final CommandGroup group = (CommandGroup) clazz.getConstructors()[0].newInstance(container);
			final GroupCommand groupCommand = getGroup(parent, clazz, group);

			result.add(groupCommand);
		}

		return result;
	}

	private static GroupCommand getGroup(GroupCommand parent, Class<?> clazz, CommandGroup group) throws ReflectiveOperationException {
		List<String> names = new ArrayList<>();

		final Commands.Name nameAnnotation = clazz.getAnnotation(Commands.Name.class);
		if (nameAnnotation != null) {
			names.add(nameAnnotation.value());
		} else {
			final String simpleName = clazz.getSimpleName();
			names.add(Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1));
		}

		final Commands.Aliases aliasesAnnotation = clazz.getAnnotation(Commands.Aliases.class);
		if (aliasesAnnotation != null) {
			names.addAll(Arrays.asList(aliasesAnnotation.value()));
		}

		List<CommandPart> subCommands = new ArrayList<>();

		final GroupCommand groupCommand = new GroupCommand(names, group, parent, subCommands, null);

		subCommands.addAll(getSingleCommands(group, groupCommand));
		subCommands.addAll(getGroupCommands(group, groupCommand));

		final List<CommandPart> defaults = subCommands.stream().filter(it -> it.isDefault).collect(Collectors.toList());
		if (defaults.size() == 1)
			groupCommand.defaultSubCommand = defaults.get(0);
		else if (defaults.size() > 1)
			throw new IllegalArgumentException("Multiple default commands.");
		return groupCommand;
	}

	private static List<SingleCommand> getSingleCommands(Object container, GroupCommand parent) {
		final Class<?> clazz = container.getClass();
		final Method[] methods = clazz.getDeclaredMethods();
		final List<SingleCommand> result = new ArrayList<>();

		for (Method method : methods) {
			final Parameter[] parameters = method.getParameters();
			if (parameters.length < 1)
				continue;

			final Parameter contextParam = parameters[0];
			if (!contextParam.getType().equals(CommandContext.class))
				continue;

			Class<?> senderClass = CommandSender.class;

			final Type pType = contextParam.getParameterizedType();
			if (pType instanceof ParameterizedType) {
				final Type[] typeArgs = ((ParameterizedType) pType).getActualTypeArguments();
				if (typeArgs.length > 0)
					senderClass = (Class<?>) typeArgs[0];
			}

			final Annotation[] annotations = method.getDeclaredAnnotations();
			String name = method.getName();
			List<String> names = new ArrayList<>();

			for (Annotation annotation : annotations) {
				if (annotation instanceof Commands.Name) {
					name = ((Commands.Name) annotation).value();
				} else if (annotation instanceof Commands.Aliases) {
					names.addAll(Arrays.asList(((Commands.Aliases) annotation).value()));
				}
			}

			names.add(0, name);
			result.add(new SingleCommand(names, container, parent, method, senderClass));
		}

		return result;
	}

	private static Command createBukkitCommand(
			CommandPart command
	) {
		final CommandConfig config;
		try {
			config = new CommandConfig(command);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		final List<String> names = command.names;
		final int nameCount = names.size();
		String name = names.get(0);
		final List<String> aliases = names.subList(1, nameCount);

		return new Command(
				name,
				"",
				"",
				aliases
		) {
			@Override
			public boolean execute(CommandSender sender, String alias, String[] rawArgs) {
				command.execute(this, config, sender, alias, new ParsingIterator(String.join(" ", rawArgs)));
				return true;
			}

			@Override
			public List<String> tabComplete(
					CommandSender sender,
					String alias,
					String[] rawArgs
			) throws IllegalArgumentException {
				int parameterStartIndex = rawArgs.length - 1;
				for (int i = 0; i < rawArgs.length - 1; i++) {
					parameterStartIndex += rawArgs[i].length();
				}

				return command.tabComplete(
						this, sender, alias,
						new ParsingIterator(String.join(" ", rawArgs)),
						parameterStartIndex
				);
			}
		};

	}
}
