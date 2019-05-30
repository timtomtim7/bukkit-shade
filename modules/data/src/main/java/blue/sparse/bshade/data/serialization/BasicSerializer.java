package blue.sparse.bshade.data.serialization;

import blue.sparse.bshade.data.Data;

public class BasicSerializer implements Serializer<Object> {

	@Override
	public boolean canSerialize(Object object) {
		return false;
	}

	@Override
	public void set(Data target, String name, Object value) {
		target.setRaw(name, value);
	}

	@Override
	public Object get(Data source, String name) {
		return source.getRaw(name);
	}

}
