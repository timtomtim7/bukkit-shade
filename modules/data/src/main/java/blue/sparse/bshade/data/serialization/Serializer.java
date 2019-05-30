package blue.sparse.bshade.data.serialization;

import blue.sparse.bshade.data.Data;

public interface Serializer<T> {

	boolean canSerialize(Object object);

	void set(Data target, String name, T value);
	T get(Data source, String name);

}
