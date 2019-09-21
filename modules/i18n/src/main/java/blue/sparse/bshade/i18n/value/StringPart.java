package blue.sparse.bshade.i18n.value;

import java.util.Map;

public class StringPart implements Part {

	private String value;

	public StringPart(String value) {
		this.value = value;
	}

	@Override
	public String toString(Map<String, Object> placeholders) {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public String raw() {
		return toString();
	}
}