package blue.sparse.bshade.i18n.value;

import java.util.Map;

public interface Part {
	String toString(Map<String, Object> placeholders);

	String raw();
}