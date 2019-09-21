package blue.sparse.bshade.command;

import blue.sparse.bshade.command.reflection.ReflectionCommandLoader;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Commands {

	private static Set<Command> registered = new HashSet<>();

	public static void registerCommands(Object parent) {
		try {
			final List<Command> bukkitCommands = ReflectionCommandLoader.getTopLevelCommands(parent);
			for (Command command : bukkitCommands) {
				try {
					registerCommand(command);
				} catch (ReflectiveOperationException e) {
					e.printStackTrace();
				}
			}
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	public static void unregisterAllCommands() {
		try {
			SimpleCommandMap map = getCommandMap();
			for (Command command : registered) {
				command.unregister(map);
			}
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private static void registerCommand(Command command) throws ReflectiveOperationException {
		getCommandMap().register(JavaPlugin.getProvidingPlugin(Commands.class).getName().toLowerCase(), command);
		registered.add(command);
	}

	private static SimpleCommandMap getCommandMap() throws NoSuchFieldException, IllegalAccessException {
		Server server = Bukkit.getServer();
		Field commandMapField = server.getClass().getDeclaredField("commandMap");
		commandMapField.setAccessible(true);
		return (SimpleCommandMap) commandMapField.get(server);
	}

	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Name {
		String value();
	}

	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Aliases {
		String[] value();
	}

	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Default {
	}

}
