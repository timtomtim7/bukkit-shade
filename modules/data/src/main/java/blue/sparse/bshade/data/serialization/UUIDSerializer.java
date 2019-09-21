package blue.sparse.bshade.data.serialization;

import blue.sparse.bshade.data.Data;

import java.util.UUID;

public class UUIDSerializer implements Serializer<UUID> {

	public static final UUIDSerializer INSTANCE = new UUIDSerializer();

	private UUIDSerializer() {
	}

	@Override
	public boolean canSerialize(Object object) {
		return object instanceof UUID;
	}

	@Override
	public void set(Data target, String name, UUID value) {
		target.setData(name, it -> {
			it.setLong("most", value.getMostSignificantBits());
			it.setLong("least", value.getLeastSignificantBits());
		});
	}

	@Override
	public UUID get(Data source, String name) {
		Data data = source.getData(name);
		return new UUID(data.getLong("most"), data.getLong("least"));
	}

	@Override
	public int getPriority() {
		return 0;
	}
}
