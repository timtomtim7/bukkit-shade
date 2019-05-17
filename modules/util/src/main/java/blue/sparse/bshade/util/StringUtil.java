package blue.sparse.bshade.util;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {

	private StringUtil() {
	}

	private static Map<String, String> toMap(Object[] objects) {
		final Map<String, String> result = new HashMap<>();

		if (objects.length % 2 != 0)
			throw new IllegalArgumentException("Mis-matched value and key count.");

		for (int i = 0; i < objects.length; i += 2) {
			Object keyObj = objects[i];
			if (!(keyObj instanceof String))
				throw new IllegalArgumentException("Keys must be strings");

			String value = String.valueOf(objects[i + 1]);
			result.put((String) keyObj, value);
		}

		return result;
	}

	/**
	 * @param format       format with placeholders in brackets: {}
	 * @param placeholders String keys and Object values, repeating: [key, value, key, value, ...]
	 * @param color If true, applies {@link StringUtil#color(String)}
	 * @return the format with placeholders replaced with the values
	 */
	public static String placeholders(
			String format,
			boolean color,
			Object... placeholders
	) {
		final Map<String, String> map = toMap(placeholders);
		final Pattern pattern = Pattern.compile("\\{([\\w\\d]+)}");
		final Matcher matcher = pattern.matcher(format);

		while (matcher.find()) {
			final String fullText = matcher.group(0);
			final String key = matcher.group(1);

			if (map.containsKey(key))
				format = format.replace(fullText, map.get(key));
		}

		if(color)
			return color(format);

		return format;
	}

	/**
	 * @param value text to apply color codes to
	 * @return the value with color codes applied
	 * @see ChatColor#translateAlternateColorCodes(char, String)
	 */
	public static String color(String value) {
		return ChatColor.translateAlternateColorCodes('&', value);
	}

	/**
	 * Formats a value with commas: <br>
	 * <code>4182</code> -> <code>"4,182",</code><br>
	 * <code>57837538</code> -> <code>"57,837,538"</code>
	 * @param value the value to format with commas
	 * @return the formatted string
	 */
	public static String commas(int value) {
		return String.format("%,d", value);
	}

	/**
	 * Formats a value with commas: <br>
	 * <code>4182</code> -> <code>"4,182",</code><br>
	 * <code>57837538</code> -> <code>"57,837,538"</code>
	 * @param value the value to format with commas
	 * @return the formatted string
	 */
	public static String commas(long value) {
		return String.format("%,d", value);
	}

	/**
	 * Formats the name of an enum as title case: <br>
	 * <code>ONE_TWO_THREE</code> -> <code>"One Two Three"</code>
	 * @param value the value that will have its name converted to title case
	 * @return the value name in title case
	 */
	public static String titleCase(Enum<?> value) {
		return titleCase(value.name());
	}

	public static String titleCase(String value) {
		final String[] split = value.toLowerCase().split("[ _]");

		for (int i = 0; i < split.length; i++) {
			String string = split[i];
			if (string.length() == 0)
				continue;
			split[i] = Character.toUpperCase(string.charAt(0)) + string.substring(1);
		}

		return String.join(" ", split);
	}

}
