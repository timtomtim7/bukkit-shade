package blue.sparse.bshade.command.reflection;

import blue.sparse.bshade.command.CommandContext;
import blue.sparse.bshade.command.Commands;
import blue.sparse.bshade.command.messages.CommandConfig;
import blue.sparse.bshade.command.parameters.ParameterRegistry;
import blue.sparse.bshade.command.parameters.annotations.Optional;
import blue.sparse.bshade.command.parameters.filter.AnnotationParameterFilter;
import blue.sparse.bshade.command.parameters.filter.ParameterFilters;
import blue.sparse.bshade.command.util.ParsingIterator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SingleCommand extends CommandPart {

	public final Object container;
	public final Method method;

	public final Class<?> senderSuperclass;

	private final Parameter[] parameters;

	protected SingleCommand(
			List<String> names,
			Object container,
			GroupCommand parent,
			Method method,
			Class<?> senderSuperclass
	) {
		super(names, parent);
		this.container = container;
		this.method = method;
		this.senderSuperclass = senderSuperclass;
		this.isDefault = method.getAnnotation(Commands.Default.class) != null;

		this.parameters = method.getParameters();
		for (int i = 1; i < parameters.length; i++) {
			Parameter param = parameters[i];
			if (ParameterRegistry.findParser(param) == null) {
				throw new UnsupportedOperationException(String.format(
						"Command parameter with no registered type. %s",
						method.toGenericString()
				));
			}
		}
	}

	@Override
	public void execute(
			Command command,
			CommandConfig config,
			CommandSender sender,
			String usedAlias,
			ParsingIterator input
	) {
		if (!senderSuperclass.isInstance(sender)) {
			config.sendWrongSenderType(this, sender);
			return;
		}

		final List<Object> args = new ArrayList<>();

		for (int i = 1; i < parameters.length; i++) {
			final Parameter param = parameters[i];
			final Object result = ParameterRegistry.parse(param, input, true, sender);

			final Set<AnnotationParameterFilter<?, ?>> failedFilters = ParameterFilters.test(sender, param, result);
			if(!failedFilters.isEmpty()) {
				for (AnnotationParameterFilter<?, ?> filter : failedFilters) {
					config.sendFilterFail(this, sender, param.getName(), filter.getAnnotationClass().getSimpleName());
				}
				return;
			}

			if (result == null && param.getAnnotation(Optional.class) == null) {
				config.sendMissingParameter(this, sender, param.getName());
				return;
			}

			args.add(result);

			input.takeWhile(Character::isWhitespace);
		}

		final CommandContext context = new CommandContext<>(
				command,
				sender,
				usedAlias,
				input.clone().takeUntil(it -> false)
		);

		args.add(0, context);

		try {
			method.invoke(container, args.toArray());
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	public void executePreParsed(
			Object[] args
	) {

		try {
			method.invoke(container, args);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	public ParseResult parse(
			Command command,
			CommandSender sender,
			String usedAlias,
			ParsingIterator input
	) {
		if (!senderSuperclass.isInstance(sender)) {
			return new ParseResult.FailSender();
		}

		final List<Object> args = new ArrayList<>();

		for (int i = 1; i < parameters.length; i++) {
			final Parameter param = parameters[i];
			final Object value = ParameterRegistry.parse(param, input, true, sender);

			final Set<AnnotationParameterFilter<?, ?>> failedFilters = ParameterFilters.test(sender, param, value);
			if(!failedFilters.isEmpty()) {
				return new ParseResult.FailParameter();
			}

			if (value == null && param.getAnnotation(Optional.class) == null) {
				final ParseResult.FailParameter result = new ParseResult.FailParameter();
				result.parameter = param;
				return result;
			}

			args.add(value);

			input.takeWhile(Character::isWhitespace);
		}

		CommandContext context = new CommandContext<>(command, sender, usedAlias, input.takeUntil(it -> false));
		args.add(0, context);

		final ParseResult.Success success = new ParseResult.Success();
		success.args = args.toArray();
		return success;
	}

	@Override
	public List<String> tabComplete(Command command, CommandSender sender, String usedAlias, ParsingIterator input, int parameterStartIndex) {
//		System.out.println("Tab complete "+usedAlias+" "+parameterStartIndex);
		if (!senderSuperclass.isInstance(sender)) {
			return Collections.emptyList();
		}

		for (int i = 1; i < parameters.length; i++) {
			final Parameter param = parameters[i];

			final int indexBeforeParse = input.getIndex();
			if (i > 1 && indexBeforeParse > 0 && !Character.isWhitespace(input.value.charAt(indexBeforeParse - 1)))
				return Collections.emptyList();

			final Object value = ParameterRegistry.parse(param, input, false, sender);
//			System.out.printf("Parameter %s (Starting at %d) -> %s%n", param.getName(), indexBeforeParse, value);

			final boolean isNotOptional = param.getAnnotation(Optional.class) == null;
			if (value == null && isNotOptional) {
				input.setIndex(indexBeforeParse);
				if (parameterStartIndex == indexBeforeParse) {
					return ParameterRegistry.getSuggestions(
							param,
							input,
							it -> ParameterFilters.test(sender, param, it).isEmpty(),
							sender
					);
				}
			}

			input.takeWhile(Character::isWhitespace);
		}

		return Collections.emptyList();
	}

	public String createSignature(
			int missingParameterIndex,
			String defaultColor,
			String missingColor,
			String afterMissingColor
	) {
		StringBuilder result = new StringBuilder();
		result.append(defaultColor);
		result.append(getSignaturePrefix());
//		result.append('/');
//		result.append(names.get(0));

		final Parameter[] parameters = method.getParameters();
		for (int i = 1; i < parameters.length; i++) {
			result.append(' ');
			if (missingParameterIndex == i) {
				result.append(missingColor);
			} else if (missingParameterIndex < i) {
				result.append(afterMissingColor);
			} else {
				result.append(defaultColor);
			}

			final Parameter param = parameters[i];

			boolean optional = param.getAnnotation(Optional.class) != null;
			if (optional) result.append('[');
			else result.append('<');

			result.append(param.getName());

			if (optional) result.append(']');
			else result.append('>');
		}

		return result.toString();
	}

}