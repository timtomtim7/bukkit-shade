package blue.sparse.bshade.command.parameters;

import blue.sparse.bshade.command.util.CharPredicate;
import blue.sparse.bshade.command.util.ParsingIterator;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class ParameterRegistry {

	private static final String NUMBER_CHARS = "0123456789-+.e";
	private static Set<ParameterType<?>> types = new LinkedHashSet<>();

	static {
		final CharPredicate notWhitespace = c -> !Character.isWhitespace(c);
		register(ParameterType.of(CharSequence.class, notWhitespace, it -> it));

		register(ParameterType.of(
				Player.class, notWhitespace, Bukkit::getPlayerExact,
				(begin, predicate) -> Bukkit.getOnlinePlayers().stream()
						.filter(it -> it.getName().toLowerCase().startsWith(begin.toLowerCase()))
						.filter(predicate)
						.map(HumanEntity::getName)
						.collect(Collectors.toList())
		));

		register(ParameterType.of(OfflinePlayer.class, notWhitespace, name -> {
			final OfflinePlayer player = Bukkit.getOfflinePlayer(name);
			if (player == null || (!player.isOnline() && !player.hasPlayedBefore()))
				return null;

			return player;
		}));

		register(new ParameterType<Enum<?>>() {
			@Override
			public boolean isApplicable(Parameter parameter) {
				return Enum.class.isAssignableFrom(parameter.getType());
			}

			@Override
			public Enum<?> parse(Parameter parameter, ParsingIterator iterator, CommandSender sender) throws Throwable {
				final String name = iterator.takeWhile(notWhitespace);
				Enum[] values = (Enum[]) parameter.getType().getEnumConstants();
				return Arrays.stream(values)
						.filter(it -> it.name().equalsIgnoreCase(name))
						.findFirst().orElse(null);
			}

			@Override
			public List<String> getSuggestions(Parameter parameter, ParsingIterator iterator, Predicate<Enum<?>> filter, CommandSender sender) throws Throwable {
				final String name = iterator.takeWhile(notWhitespace).toUpperCase();
				Enum[] values = (Enum[]) parameter.getType().getEnumConstants();
				return Arrays.stream(values)
						.filter(filter::test)
						.filter(it -> it.name().startsWith(name))
						.map(anEnum -> anEnum.name().toLowerCase())
						.collect(Collectors.toList());
			}
		});

		register(ParameterType.of(Character.class, ParsingIterator::next));
		register(ParameterType.of(Character.TYPE, ParsingIterator::next));

		register(ParameterType.of(Boolean.class, notWhitespace, ParameterRegistry::parseBoolean, ParameterRegistry::tabCompleteBoolean));
		register(ParameterType.of(Boolean.TYPE, notWhitespace, ParameterRegistry::parseBoolean, ParameterRegistry::tabCompleteBoolean));

		registerNumberParser(Byte.class, Byte.TYPE, Byte::valueOf);
		registerNumberParser(Short.class, Short.TYPE, Short::valueOf);
		registerNumberParser(Integer.class, Integer.TYPE, Integer::valueOf);
		registerNumberParser(Long.class, Long.TYPE, Long::valueOf);
		registerNumberParser(Float.class, Float.TYPE, Float::valueOf);
		registerNumberParser(Double.class, Double.TYPE, Double::valueOf);
	}

	private ParameterRegistry() {
	}

	public static void register(ParameterType<?> type) {
		types.add(type);
	}

	public static ParameterType<?> findParser(Parameter parameter) {
		return types.stream()
				.filter(it -> it.isApplicable(parameter))
				.findAny().orElse(null);
	}

	public static Object parse(Parameter parameter, ParsingIterator input, boolean resetIndexIfFail, CommandSender sender) {
		final ParameterType<?> parser = findParser(parameter);
		final int index = input.getIndex();

		Object result = null;

		try {
			result = parser.parse(parameter, input, sender);
		} catch (Throwable ignored) {
		}

		if (resetIndexIfFail && result == null)
			input.setIndex(index);

		return result;
	}

	public static List<String> getSuggestions(Parameter parameter, ParsingIterator input, Predicate<Object> filter, CommandSender sender) {
		final ParameterType<?> parser = findParser(parameter);
		final int index = input.getIndex();

		List<String> result = null;

		try {
			result = parser.getSuggestions(parameter, input, filter::test, sender);
		} catch (Throwable ignored) {
//			ignored.printStackTrace();
		}

		if (result == null)
			input.setIndex(index);

		return result == null ? Collections.emptyList() : result;
	}

	private static Boolean parseBoolean(String it) {
		if (it.equalsIgnoreCase("true"))
			return true;
		else if (it.equalsIgnoreCase("false"))
			return false;
		else
			throw new IllegalArgumentException();
	}

	private static <T> void registerNumberParser(
			Class<T> clazz1,
			Class<T> clazz2,
			Function<String, T> toNumber
	) {
		final Function<ParsingIterator, T> function = (iterator) -> {
			String string = iterator.takeWhile(it -> NUMBER_CHARS.indexOf(it) != -1);
			while (string.endsWith(".")) {
				string = string.substring(0, string.length() - 1);
				iterator.previous();
			}

			if (string.isEmpty())
				throw new IllegalArgumentException();

			return toNumber.apply(string);
		};
		register(ParameterType.of(clazz1, function));
		register(ParameterType.of(clazz2, function));
	}

	private static List<String> tabCompleteBoolean(String begin, Predicate<Boolean> predicate) {
		final List<String> result = new ArrayList<>();

		begin = begin.toLowerCase();
		if("true".startsWith(begin) && predicate.test(true)) result.add("true");
		if("false".startsWith(begin) && predicate.test(false)) result.add("false");

		return result;
	}
}
