package blue.sparse.bshade.command.parameters.filter;

import blue.sparse.bshade.command.parameters.annotations.HasPermission;
import blue.sparse.bshade.command.parameters.annotations.NoPermission;
import blue.sparse.bshade.command.util.TriPredicate;
import org.bukkit.command.CommandSender;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public final class ParameterFilters {

	//	@SuppressWarnings("UnstableApiUsage")
//	private static Multimap<Class<? extends Annotation>, AnnotationParameterFilter<?, ?>> filters = MultimapBuilder.hashKeys().hashSetValues().build();
	private static Map<Class<? extends Annotation>, Set<AnnotationParameterFilter<?, ?>>> filters = new HashMap<>();

	static {
		register(
				HasPermission.class,
				CommandSender.class,
				(a, v) -> v.hasPermission(a.value())
		);

		register(
				NoPermission.class,
				CommandSender.class,
				(a, v) -> !v.hasPermission(a.value())
		);
	}

	public static void register(AnnotationParameterFilter<?, ?> filter) {
		filters.computeIfAbsent(filter.getAnnotationClass(), clazz -> new HashSet<>()).add(filter);
//		filters.put(filter.getAnnotationClass(), filter);
	}

	public static Set<AnnotationParameterFilter<?, ?>> test(CommandSender sender, Parameter parameter, Object value) {
		final Annotation[] annotations = parameter.getAnnotations();

		Set<AnnotationParameterFilter<?, ?>> result = Collections.emptySet();

		for (Annotation annotation : annotations) {
			final List<AnnotationParameterFilter<?, ?>> filters = ParameterFilters.filters
					.keySet().stream()
					.filter(it -> it.isInstance(annotation))
					.flatMap(it -> ParameterFilters.filters.getOrDefault(it, Collections.emptySet()).stream())
					.filter(it -> it.getValueClass().isInstance(value))
					.collect(Collectors.toList());

			for (AnnotationParameterFilter<?, ?> filter : filters) {
				if (!castInvoke(filter, sender, annotation, value)) {
					if (result.isEmpty()) {
						result = Collections.singleton(filter);
						continue;
					}

					if (result.size() == 1)
						result = new HashSet<>(result);

					result.add(filter);
				}
			}
		}

		return result;
	}

	public static boolean isRegistered(Class<? extends Annotation> annotationClass) {
		return filters.containsKey(annotationClass);
	}

	public static <A extends Annotation, V> void register(
			Class<A> annotationClass,
			Class<V> valueClass,
			BiPredicate<A, V> filter
	) {
		register(annotationClass, valueClass, (s, a, v) -> filter.test(a, v));
	}

	public static <A extends Annotation, V> void register(
			Class<A> annotationClass,
			Class<V> valueClass,
			TriPredicate<CommandSender, A, V> filter
	) {
		register(new AnnotationParameterFilter<A, V>() {

			@Override
			public Class<A> getAnnotationClass() {
				return annotationClass;
			}

			@Override
			public Class<V> getValueClass() {
				return valueClass;
			}

			@Override
			public boolean filter(CommandSender sender, A annotation, V value) {
				return filter.test(sender, annotation, value);
			}
		});
	}

	private static <A extends Annotation, V> boolean castInvoke(
			AnnotationParameterFilter<A, V> filter,
			CommandSender sender,
			Annotation annotation,
			Object value
	) {
		final A a = filter.getAnnotationClass().cast(annotation);
		final V v = filter.getValueClass().cast(value);

		return filter.filter(sender, a, v);
	}

}
