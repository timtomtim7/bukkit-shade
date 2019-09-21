package blue.sparse.bshade.data.serialization;

import blue.sparse.bshade.data.Data;

import java.io.NotSerializableException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class DataSerialization {

	private static Map<String, Serializer<?>> serializers = new HashMap<>();
	private static String SERIALIZER_PREFIX = "$$";

	static {
		register("*", new ReflectiveSerializer());
		register("uuid", UUIDSerializer.INSTANCE);
		register(".", new BasicSerializer());
	}

	public static void register(String key, Serializer<?> serializer) {
		serializers.put(key, serializer);
	}

	public static void set(Data target, String name, Object value) {
		List<Map.Entry<String, Serializer<?>>> valid = serializers
				.entrySet()
				.stream()
				.filter(it -> it.getValue().canSerialize(value))
				.collect(Collectors.toList());
//		valid.forEach(it -> System.out.println(it.getValue().getClass().getName()));

		Map.Entry<String, Serializer<?>> serializerEntry = valid
				.stream()
				.max(Comparator.comparing(Map.Entry::getValue))
				.orElse(null);

		if(serializerEntry == null)
			throw new RuntimeException(new NotSerializableException());

		Serializer serializer = serializerEntry.getValue();

		if (serializer == null)
			throw new RuntimeException(new NotSerializableException());

//		System.out.printf("Using %s serializer for %s %s%n", serializer.getClass().getName(), value.getClass(), value);

		if(!(serializer instanceof BasicSerializer)) {
			target.setString(
					SERIALIZER_PREFIX + name,
					serializerEntry.getKey()
			);
		}
		serializer.set(target, name, value);
	}

	public static Object read(Data source, String name) {
		Serializer<?> serializer;
		if(source.containsKey(SERIALIZER_PREFIX + name)) {
			String serializerName = source.getString(SERIALIZER_PREFIX + name);
			serializer = serializers.get(serializerName);

			if(serializer == null)
				throw new IllegalStateException("Missing used serializer for deserialization: "+serializerName);
		}else{
			serializer = serializers.get(".");
		}

		return serializer.get(source, name);
	}

	private DataSerialization() {
	}

}
