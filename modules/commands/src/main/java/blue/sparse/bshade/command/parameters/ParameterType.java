package blue.sparse.bshade.command.parameters;

import blue.sparse.bshade.command.util.CharPredicate;
import blue.sparse.bshade.command.util.ParsingIterator;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ParameterType<T> {

	static <T> ParameterType<T> of(
			Class<T> clazz,
			Function<ParsingIterator, T> body
	) {
		return new ParameterType<T>() {
			@Override
			public boolean isApplicable(Parameter parameter) {
				return clazz.isAssignableFrom(parameter.getType());
			}

			@Override
			public T parse(Parameter target, ParsingIterator iterator, CommandSender sender) {
				return body.apply(iterator);
			}

			@Override
			public List<String> getSuggestions(Parameter parameter, ParsingIterator iterator, Predicate<T> filter, CommandSender sender) {
				return null;
			}
		};
	}

	static <T> ParameterType<T> of(
			Class<T> clazz,
			CharPredicate filter,
			Function<String, T> body
	) {
		return ParameterType.of(
				clazz,
				(iterator) -> {
					final String s = iterator.takeWhile(filter).trim();
					if (s.isEmpty())
						throw new IllegalArgumentException();
					return body.apply(s);
				}
		);
	}

	static <T> ParameterType<T> of(
			Class<T> clazz,
			Function<ParsingIterator, T> body,
			SuggestionProvider<T> suggestions

	) {
		return new ParameterType<T>() {
			@Override
			public boolean isApplicable(Parameter parameter) {
				return clazz.isAssignableFrom(parameter.getType());
			}

			@Override
			public T parse(Parameter target, ParsingIterator iterator, CommandSender sender) {
				return body.apply(iterator);
			}

			@Override
			public List<String> getSuggestions(Parameter parameter, ParsingIterator iterator, Predicate<T> filter, CommandSender sender) {
				return suggestions.getSuggestions(parameter, iterator, filter);
			}
		};
	}

	static <T> ParameterType<T> of(
			Class<T> clazz,
			CharPredicate valueFilter,
			Function<String, T> body,
			BiFunction<String, Predicate<T>, List<String>> suggestions
	) {
		return ParameterType.of(
				clazz,
				(iterator) -> {
					final String s = iterator.takeWhile(valueFilter).trim();
					if (s.isEmpty())
						throw new IllegalArgumentException();
					return body.apply(s);
				},
				(param, iterator, filter) -> suggestions.apply(iterator.takeWhile(valueFilter).trim(), filter)
		);
	}

	boolean isApplicable(Parameter parameter);

	T parse(Parameter parameter, ParsingIterator iterator, CommandSender sender) throws Throwable;

	List<String> getSuggestions(
			Parameter parameter,
			ParsingIterator iterator,
			Predicate<T> filter,
			CommandSender sender) throws Throwable;

}
