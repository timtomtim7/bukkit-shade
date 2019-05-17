package blue.sparse.bshade.command.parameters;

import blue.sparse.bshade.command.util.ParsingIterator;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.Predicate;

public interface SuggestionProvider<T> {
	List<String> getSuggestions(Parameter parameter, ParsingIterator iterator, Predicate<T> filter);
}
