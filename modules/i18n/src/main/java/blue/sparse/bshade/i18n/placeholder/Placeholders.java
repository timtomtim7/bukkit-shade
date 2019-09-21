package blue.sparse.bshade.i18n.placeholder;

import blue.sparse.bshade.i18n.placeholder.impl.FieldPlaceholders;
import blue.sparse.bshade.i18n.placeholder.impl.MapPlaceholders;

import java.util.Map;

public interface Placeholders {

	String get(String key);

	@SuppressWarnings({"unchecked", "CastCanBeRemovedNarrowingVariableType"})
	static Placeholders of(Object object) {
		if(object instanceof Placeholders)
			return (Placeholders) object;

		if(object instanceof Map) {
			Map<?, ?> map = (Map) object;
			if (map.keySet().stream().allMatch(it -> it instanceof String))
				return new MapPlaceholders((Map<String, Object>) map);
		}

		return new FieldPlaceholders(null, object);
	}

	static Placeholders map(Object... keysToValues) {
		return new MapPlaceholders(keysToValues);
	}

	static Placeholders map(Map<String, Object> map) {
		return new MapPlaceholders(map);
	}

	static Placeholders fields(String prefix, Object object) {
		return new FieldPlaceholders(prefix, object);
	}

}
