package blue.sparse.bshade.i18n.value;

import java.util.Map;

public class SimplePlaceholderPart implements Part {

	private String key;

	public SimplePlaceholderPart(String key) {
		this.key = key;
	}

	@Override
	public String toString(Map<String, Object> placeholders) {
		return String.valueOf(placeholders.get(key));
	}

	@Override
	public String toString() {
		return '{' + key + '}';
	}

	@Override
	public String raw() {
		return toString();
	}
}