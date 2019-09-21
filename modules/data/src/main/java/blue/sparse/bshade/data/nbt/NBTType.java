package blue.sparse.bshade.data.nbt;

import blue.sparse.bshade.data.Data;
import blue.sparse.bshade.data.list.RawList;
import com.google.common.primitives.Primitives;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class NBTType<E extends NBTElement> {
	private static Map<Class<?>, NBTType<?>> rawClasses = new HashMap<>();
	private static Map<NBTType<?>, Class<? extends NBTElement>> typeClasses = new HashMap<>();
	private static Map<Class<?>, Class<?>> primitives = new HashMap<>();
	private static List<NBTType<?>> types = new ArrayList<>();

	private static void initClasses() {
		primitives.put(Byte.class, Byte.TYPE);
		primitives.put(Short.class, Short.TYPE);
		primitives.put(Integer.class, Integer.TYPE);
		primitives.put(Long.class, Long.TYPE);
		primitives.put(Float.class, Float.TYPE);
		primitives.put(Double.class, Double.TYPE);

		rawClasses.put(Byte.class, NBTByte.Type.INSTANCE);
		rawClasses.put(Byte.TYPE, NBTByte.Type.INSTANCE);
		rawClasses.put(Short.class, NBTShort.Type.INSTANCE);
		rawClasses.put(Short.TYPE, NBTShort.Type.INSTANCE);
		rawClasses.put(Integer.class, NBTInteger.Type.INSTANCE);
		rawClasses.put(Integer.TYPE, NBTInteger.Type.INSTANCE);
		rawClasses.put(Long.class, NBTLong.Type.INSTANCE);
		rawClasses.put(Long.TYPE, NBTLong.Type.INSTANCE);
		rawClasses.put(Float.class, NBTFloat.Type.INSTANCE);
		rawClasses.put(Float.TYPE, NBTFloat.Type.INSTANCE);
		rawClasses.put(Double.class, NBTDouble.Type.INSTANCE);
		rawClasses.put(Double.TYPE, NBTDouble.Type.INSTANCE);
		rawClasses.put(byte[].class, NBTByteArray.Type.INSTANCE);
		rawClasses.put(int[].class, NBTIntegerArray.Type.INSTANCE);
		rawClasses.put(long[].class, NBTLongArray.Type.INSTANCE);
		rawClasses.put(String.class, NBTString.Type.INSTANCE);
		rawClasses.put(Data.class, NBTCompound.Type.INSTANCE);
		rawClasses.put(RawList.class, NBTList.Type.INSTANCE);

		typeClasses.put(NBTByte.Type.INSTANCE, NBTByte.class);
		typeClasses.put(NBTShort.Type.INSTANCE, NBTShort.class);
		typeClasses.put(NBTInteger.Type.INSTANCE, NBTInteger.class);
		typeClasses.put(NBTLong.Type.INSTANCE, NBTLong.class);
		typeClasses.put(NBTFloat.Type.INSTANCE, NBTFloat.class);
		typeClasses.put(NBTDouble.Type.INSTANCE, NBTDouble.class);
		typeClasses.put(NBTByteArray.Type.INSTANCE, NBTByteArray.class);
		typeClasses.put(NBTIntegerArray.Type.INSTANCE, NBTIntegerArray.class);
		typeClasses.put(NBTLongArray.Type.INSTANCE, NBTLongArray.class);
		typeClasses.put(NBTString.Type.INSTANCE, NBTString.class);
		typeClasses.put(NBTCompound.Type.INSTANCE, NBTCompound.class);
		typeClasses.put(NBTList.Type.INSTANCE, NBTList.class);

		types.addAll(typeClasses.keySet());
		types.sort(Comparator.comparingInt(it -> it.typeID));
	}

	public static NBTType<?> getType(int typeID) {
		if (rawClasses.isEmpty())
			initClasses();

		final NBTType<?> result = types.get(typeID - 1);
		if(result.typeID == typeID)
			return result;

		return types.stream().filter(it -> it.typeID == typeID).findFirst().orElse(null);
	}

	@SuppressWarnings("unchecked")
	public static NBTElement fromRawToNBT(Object raw) {
		if (rawClasses.isEmpty())
			initClasses();

		if (raw instanceof NBTElement)
			return (NBTElement) raw;

		if (raw instanceof Collection) {
			Collection<Object> collection = (Collection<Object>) raw;
			List<NBTElement> elements = collection
					.stream()
					.map(NBTType::fromRawToNBT)
					.collect(Collectors.toList());

			if (elements.isEmpty()) {
				return new NBTList<>(NBTCompound.Type.INSTANCE, new ArrayList<>());
			} else if (elements.stream().anyMatch(Objects::isNull)) {
				throw new IllegalArgumentException("Objects in list cannot be null.");
			} else {
				final NBTType type = elements.get(0).type;
				if(elements.stream().anyMatch(it -> it.type != type))
					throw new IllegalArgumentException("Elements in list must all have the same type.");

				return new NBTList(type, elements);
			}
		}

		if(raw instanceof RawList)
			return fromRawToNBT(((RawList) raw).toList());

		Class<?> clazz = raw.getClass();

		Class<?> rawClass = rawClasses
				.keySet()
				.stream()
				.filter(it -> it.isAssignableFrom(clazz))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Not an NBT primitive: "+clazz.getName()));

		Class<?> primitive = primitives.get(rawClass);
		if(primitive != null)
			rawClass = primitive;

//		if(rawClass == Byte.class)
//			rawClass = Byte.TYPE;
//		else if(rawClass == Short.class)
//			rawClass = Short.TYPE

//		rawClass = Primitives.unwrap(rawClass);

		final NBTType<?> type = rawClasses.get(rawClass);

		final Class<? extends NBTElement> typeClazz = typeClasses.get(type);
		try {
			return typeClazz.getConstructor(rawClass).newInstance(raw);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public final int typeID;

	public NBTType(int typeID) {
		this.typeID = typeID;
	}

	public abstract void write(E value, DataOutputStream out) throws IOException;

	public abstract E read(DataInputStream inp) throws IOException;

}
