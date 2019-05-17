package blue.sparse.bshade.command.messages;

import blue.sparse.bshade.command.parameters.filter.ParameterFilters;
import blue.sparse.bshade.command.reflection.CommandPart;
import blue.sparse.bshade.command.reflection.GroupCommand;
import blue.sparse.bshade.command.reflection.SingleCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandConfig {

//	public static final String NORMAL_COLOR_1 = "&3";
//	public static final String NORMAL_COLOR_2 = "&b";
//	public static final String NORMAL_COLOR_3 = "&7&o";
//	public static final String ERROR_COLOR_1 = "&c&l";
//	public static final String ERROR_COLOR_2 = "&c";

	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\d");

	public final String name;
	public final String filePath;
	public final File file;
	public final CommandPart command;

	private final YamlConfiguration config;

	public CommandConfig(CommandPart command) throws IOException {
		this.command = command;
		final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(CommandConfig.class);

		this.name = command.names.get(0);
		this.filePath = String.format("commands/%s.yml", name);

		final File folder = new File(plugin.getDataFolder(), "commands");
		folder.mkdirs();

		this.file = new File(folder, name + ".yml");

		if(!file.exists()) {
			try {
				plugin.saveResource(filePath, false);
			} catch (IllegalArgumentException ignored) {
			}
		}

		final YamlConfiguration defaults = new YamlConfiguration();
		defaults.set("colors", Arrays.asList("&3&l", "&3", "&b", "&7&o", "&c&l", "&c"));
		createDefault(defaults, command);

		final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.options().copyDefaults(true);
		config.options().header(
				"Configuration for "+command.names.get(0)+" command.\n\n" +
						"Colors are represented by a dollar sign followed by a number (ex. $1, $2, $3).\n" +
						"The number represents which color from the list below will be used.\n" +
						"$1 is the first color in the list, $2 is the second color, $3 is the third, etc.");
		config.setDefaults(defaults);
		config.save(file);

		this.config = config;
	}

	private static String buildKey(CommandPart command, String additional) {
		final StringBuilder result = new StringBuilder();
		buildKey(command, result);

		if (additional != null) {
			if (result.length() != 0)
				result.append('.');

			result.append(additional);
		}

		return result.toString();
	}

	private static void buildKey(CommandPart command, StringBuilder builder) {
		if (command.parent == null)
			return;

		if (builder.length() != 0)
			builder.append('.');

		buildKey(command.parent, builder);
		builder.append(".subcommands.");
		builder.append(command.names.get(0));
	}

	private YamlConfiguration createDefault(YamlConfiguration initial, CommandPart command) {
		YamlConfiguration result;

		if (command instanceof GroupCommand) {
			result = createForGroup(initial, (GroupCommand) command);
		} else if (command instanceof SingleCommand) {
			result = createForSingle(initial, (SingleCommand) command);
		} else {
			throw new UnsupportedOperationException("Cannot create default config for unknown command type.");
		}

		return result;
	}

	private YamlConfiguration createForGroup(YamlConfiguration initial, GroupCommand command) {
		final String groupName = command.names.get(0);

		final List<String> help = new ArrayList<>();
		help.add("$1Help:");

		for (CommandPart subCommand : command.subCommands) {
			final String name = subCommand.names.get(0);
			initial.set("subcommands." + name, createDefault(new YamlConfiguration(), subCommand));

			if (subCommand instanceof SingleCommand) {
				help.add("$2- " + ((SingleCommand) subCommand).createSignature(0, "$3", "$4", "$4"));
			} else {
				//TODO: Subcommand parameters?
				help.add(String.format("$2- $3/%s %s $4<subcommand> [args...]", groupName, name));
			}
		}

		initial.set("invalidSubCommand", help);

		return initial;
	}

	private YamlConfiguration createForSingle(YamlConfiguration initial, SingleCommand command) {
		if (command.senderSuperclass != CommandSender.class) {
			initial.set("wrongSenderType", String.format(
					"$6Only %ss can run this command.",
					command.senderSuperclass.getSimpleName()
			));
		}

		final Parameter[] parameters = command.method.getParameters();

		for (int i = 1; i < parameters.length; i++) {
			final Parameter param = parameters[i];
			initial.set("missing." + param.getName(), "$1Usage: " + command.createSignature(
					i, "$3", "$5", "$6"
			));

			final Annotation[] annotations = param.getAnnotations();
			for (Annotation annotation : annotations) {
				if (!ParameterFilters.isRegistered(annotation.annotationType()))
					continue;

				final String annotationName = annotation.annotationType().getSimpleName();
				initial.set(String.format(
						"filter.%s.%s",
						param.getName(),
						annotationName
				), "$6Failed to pass filter: " + annotationName);
			}
		}

		return initial;
	}

	public void sendWrongSenderType(CommandPart command, CommandSender target) {
		send(command, "wrongSenderType", target);
	}

	public void sendInvalidSubCommand(CommandPart command, CommandSender target) {
		send(command, "invalidSubCommand", target);
	}

	public void sendMissingParameter(CommandPart command, CommandSender target, String parameterName) {
		send(command, "missing." + parameterName, target);
	}

	public void sendFilterFail(CommandPart command, CommandSender target, String parameterName, String annotationName) {
		send(command, String.format("filter.%s.%s", parameterName, annotationName), target);
	}

	private String color(String input) {
		final Matcher matcher = PLACEHOLDER_PATTERN.matcher(input);
		final List<?> colors = config.getList("colors");

		while(matcher.find()) {
			final String text = matcher.group();
			final int index = Integer.valueOf(text.substring(1));
			if(index > colors.size())
				continue;

			input = input.replace(text, colors.get(index - 1).toString());
		}

		input = ChatColor.translateAlternateColorCodes('&', input);
		return input;
	}

	private void send(CommandPart command, String additional, CommandSender target) {
		final String key = buildKey(command, additional);
		if (!config.contains(key)) {
			target.sendMessage("\u00a7cA message is missing from the config. (" + key + ")");
			return;
		}

		if (config.isList(key)) {
			config.getList(key).stream()
					.map(String::valueOf)
					.map(this::color)
					.forEach(target::sendMessage);

			return;
		}

		final String message = String.valueOf(config.get(key));
		target.sendMessage(color(message));
	}


//	public static class PathBuilder {
//		private final CommandPart command;
//		private final String key;
//
//		private PathBuilder(CommandPart command) {
//			this.command = command;
//			final StringBuilder builder = new StringBuilder();
//			buildKey(command, builder);
//			this.key = builder.toString();
//		}
//
//		public String getKey() {
//			return key;
//		}
//
//		public String wrongSenderType(Configuration config) {
//			return null;
//		}
//
//
//	}

}
