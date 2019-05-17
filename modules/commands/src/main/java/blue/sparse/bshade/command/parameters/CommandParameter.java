package blue.sparse.bshade.command.parameters;

import blue.sparse.bshade.command.Commands;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

public interface CommandParameter {

	Annotation[] getAnnotations();

	Type getType();

	default Class<?> getTypeClass() {
		final Type type = getType();
		if (type instanceof Class) {
			return (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			return (Class<?>) ((ParameterizedType) type).getRawType();
		}

		throw new UnsupportedOperationException();
	}

	default <T extends Annotation> T getAnnotation(Class<T> clazz) {
		return Arrays.stream(getAnnotations())
				.filter(clazz::isInstance)
				.findFirst()
				.map(clazz::cast)
				.orElse(null);
	}

}
