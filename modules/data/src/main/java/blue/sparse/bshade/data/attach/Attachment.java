package blue.sparse.bshade.data.attach;

public interface Attachment<T> {

	T getAttachedTo();

	static void add(Class<? extends Attachment<?>> clazz) {

	}

	static <T extends Attachment<V>, V> T get(V attached, Class<T> clazz) {
		throw new UnsupportedOperationException();
	}

}
