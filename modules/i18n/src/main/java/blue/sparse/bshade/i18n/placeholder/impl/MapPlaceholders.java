package blue.sparse.bshade.i18n.placeholder.impl;

import blue.sparse.bshade.i18n.placeholder.Placeholders;
import blue.sparse.bshade.i18n.util.MapUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class MapPlaceholders implements Placeholders {

	private final Map<String, Object> map;

	public MapPlaceholders(Map<String, Object> map) {
		this.map = map;
	}

	public MapPlaceholders(Object... keysToValues) {
		this(MapUtil.toMap(keysToValues));
	}

	@Override
	public String get(String key) {
		return String.valueOf(map.get(key));
	}
}
