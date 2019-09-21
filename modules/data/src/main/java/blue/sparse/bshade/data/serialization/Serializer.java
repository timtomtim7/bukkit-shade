package blue.sparse.bshade.data.serialization;

import blue.sparse.bshade.data.Data;

public interface Serializer<T> extends Comparable<Serializer> {

	boolean canSerialize(Object object);

	void set(Data target, String name, T value);

	T get(Data source, String name);

	default int getPriority() {
		return 0;
	}

	@Override
	default int compareTo(Serializer o) {
		return Integer.compare(getPriority(), o.getPriority());
	}
}
