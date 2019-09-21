package blue.sparse.bshade.i18n.util;

import java.util.HashMap;
import java.util.Map;

public final class MapUtil {

	private MapUtil() {}

	public static Map<String, Object> toMap(Object[] objects) {
		final Map<String, Object> result = new HashMap<>();

		if (objects.length % 2 != 0)
			throw new IllegalArgumentException("Mis-matched value and key count.");

		for (int i = 0; i < objects.length; i += 2) {
			Object keyObj = objects[i];
			if (!(keyObj instanceof String))
				throw new IllegalArgumentException("Keys must be strings");

			result.put((String) keyObj, objects[i + 1]);
		}

		return result;
	}

}
