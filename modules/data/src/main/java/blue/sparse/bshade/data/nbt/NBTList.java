package blue.sparse.bshade.data.nbt;

import blue.sparse.bshade.data.list.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class NBTList<E extends NBTElement<?, ?>> extends NBTElement<NBTList.Type, RawList<?>> {

	private NBTType<E> valueType;
	private List<E> value;

	public NBTList(NBTType<E> valueType, List<E> value) {
		super(Type.INSTANCE);
		this.valueType = valueType;
		this.value = value;
	}

	@Override
	public RawList<?> getRawValue() {
		return type.toRawList(this);
	}

	public List<E> getValue() {
		return value;
	}

	public void setValue(List<E> value) {
		this.value = value;
	}

	public static class Type extends NBTType<NBTList<?>> {

		public static final Type INSTANCE = new Type();
		private Map<NBTType<?>, Class<? extends RawList>> listTypes = new HashMap<>();

		private Type() {
			super(9);

			listTypes.put(NBTByte.Type.INSTANCE, ByteList.class);
			listTypes.put(NBTShort.Type.INSTANCE, ShortList.class);
			listTypes.put(NBTInteger.Type.INSTANCE, IntList.class);
			listTypes.put(NBTLong.Type.INSTANCE, LongList.class);

			listTypes.put(NBTFloat.Type.INSTANCE, FloatList.class);
			listTypes.put(NBTDouble.Type.INSTANCE, DoubleList.class);

			listTypes.put(NBTString.Type.INSTANCE, StringList.class);

			listTypes.put(NBTByteArray.Type.INSTANCE, ByteArrayList.class);
			listTypes.put(NBTIntegerArray.Type.INSTANCE, IntArrayList.class);
			listTypes.put(NBTLongArray.Type.INSTANCE, LongArrayList.class);

			listTypes.put(NBTCompound.Type.INSTANCE, DataList.class);
			listTypes.put(Type.INSTANCE, RawListList.class);
		}

		private RawList<?> toRawList(NBTList<?> list) {
			Class<? extends RawList> clazz = listTypes.get(list.valueType);
			final List<?> rawValues = list.value.stream()
					.map(NBTElement::getRawValue)
					.collect(Collectors.toList());

			try {
				return (RawList<?>) clazz
						.getConstructor(Collection.class)
						.newInstance(rawValues);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void write(NBTList<?> value, DataOutputStream out) throws IOException {
			out.write(value.valueType.typeID);
			out.writeInt(value.value.size());
			for (NBTElement element : value.value) {
				element.type.write(element, out);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public NBTList<?> read(DataInputStream inp) throws IOException {
			int typeID = inp.read();
			int size = inp.readInt();

			NBTType<?> type = NBTType.getType(typeID);
			List<NBTElement<?, ?>> result = new ArrayList<>();

			for (int i = 0; i < size; i++) {
				result.add(type.read(inp));
			}

			return new NBTList(type, result);
		}
	}

}
