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
import java.util.List;

public class Commands {

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

	private static void registerCommand(Command command) throws ReflectiveOperationException {
		Server server = Bukkit.getServer();
		Field commandMapField = server.getClass().getDeclaredField("commandMap");
		commandMapField.setAccessible(true);
		final SimpleCommandMap commandMap = ((SimpleCommandMap) commandMapField.get(server));

		commandMap.register(JavaPlugin.getProvidingPlugin(Commands.class).getName().toLowerCase(), command);
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
	public @interface Default { }

}
