package blue.sparse.bshade.i18n;

import blue.sparse.bshade.i18n.value.LocaleValue;
import org.bukkit.command.CommandSender;

import java.util.Map;

public interface LocaleKey {

	LocaleValue get();

	String name();

	default void send(CommandSender target, Map<String, Object> placeholders) {
		target.sendMessage(get().toString(placeholders));
	}

	default void send(CommandSender target, Object... placeholders) {
		target.sendMessage(get().toString(placeholders));
	}

	default void send(CommandSender target, Object placeholder) {
		
	}

}
