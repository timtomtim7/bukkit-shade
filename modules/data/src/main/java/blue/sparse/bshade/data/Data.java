package blue.sparse.bshade.data;

import blue.sparse.bshade.data.list.*;
import blue.sparse.bshade.data.nbt.NBTCompound;
import blue.sparse.bshade.data.serialization.DataSerialization;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public interface Data {

	static Data create() {
		return new NBTCompound();
	}

	static Data create(Consumer<Data> consumer) {
		Data result = create();
		consumer.accept(result);
		return result;
	}

	static Data readBinary(File file) {
		try {
			return NBTCompound.Type.INSTANCE.readFile(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static Data readBinary(File file, Consumer<Data> consumer) {
		Data result = readBinary(file);
		consumer.accept(result);
		return result;
	}

	Collection<String> keys();
	int size();

	default boolean containsKey(String key) {
		return keys().contains(key);
	}

	Object getRawOrNull(String name);

	default Object getRaw(String name) {
		final Object raw = getRawOrNull(name);
		if(raw == null)
			throw new NoSuchElementException(name);

		return raw;
	}

	@SuppressWarnings("unchecked")
	default <T> T getSerializedTyped(String name) {
		return (T) getSerialized(name);
	}

	default Object getSerialized(String name) {
		return DataSerialization.read(this, name);
	}

	default byte getByte(String name) {
		return (byte) getRaw(name);
	}

	default short getShort(String name) {
		return (short) getRaw(name);
	}

	default int getInt(String name) {
		return (int) getRaw(name);
	}

	default long getLong(String name) {
		return (long) getRaw(name);
	}

	default float getFloat(String name) {
		return (float) getRaw(name);
	}

	default double getDouble(String name) {
		return (double) getRaw(name);
	}

	default byte[] getByteArray(String name) {
		return (byte[]) getRaw(name);
	}

	default int[] getIntArray(String name) {
		return (int[]) getRaw(name);
	}

	default long[] getLongArray(String name) {
		return (long[]) getRaw(name);
	}

	default String getString(String name) {
		return (String) getRaw(name);
	}

	default Data getData(String name) {
		return (Data) getRaw(name);
	}

	default RawList<?> getRawList(String name) {
		return (RawList<?>) getRaw(name);
	}

	default ByteList getByteList(String name) {
		return (ByteList) getRaw(name);
	}

	default ShortList getShortList(String name) {
		return (ShortList) getRaw(name);
	}

	default IntList getIntList(String name) {
		return (IntList) getRaw(name);
	}

	default LongList getLongList(String name) {
		return (LongList) getRaw(name);
	}

	default FloatList getFloatList(String name) {
		return (FloatList) getRaw(name);
	}

	default DoubleList getDoubleList(String name) {
		return (DoubleList) getRaw(name);
	}

	default ByteArrayList getByteArrayList(String name) {
		return (ByteArrayList) getRaw(name);
	}

	default IntArrayList getIntArrayList(String name) {
		return (IntArrayList) getRaw(name);
	}

	default LongArrayList getLongArrayList(String name) {
		return (LongArrayList) getRaw(name);
	}

	default StringList getStringList(String name) {
		return (StringList) getRaw(name);
	}

	default DataList getDataList(String name) {
		return (DataList) getRaw(name);
	}


	Data setRaw(String name, Object value);

	default Data setSerialized(String name, Object value) {
		DataSerialization.set(this, name, value);
		return this;
	}

	default Data setByte(String name, byte value) {
		setRaw(name, value);
		return this;
	}

	default Data setShort(String name, short value) {
		setRaw(name, value);
		return this;
	}

	default Data setInt(String name, int value) {
		setRaw(name, value);
		return this;
	}

	default Data setLong(String name, long value) {
		setRaw(name, value);
		return this;
	}

	default Data setFloat(String name, float value) {
		setRaw(name, value);
		return this;
	}

	default Data setDouble(String name, double value) {
		setRaw(name, value);
		return this;
	}

	default Data setByteArray(String name, byte... value) {
		setRaw(name, value);
		return this;
	}

	default Data setIntArray(String name, int... value) {
		setRaw(name, value);
		return this;
	}

	default Data setLongArray(String name, long... value) {
		setRaw(name, value);
		return this;
	}

	default Data setString(String name, String value) {
		setRaw(name, value);
		return this;
	}

	default Data setData(String name, Data value) {
		setRaw(name, value);
		return this;
	}

	default Data setRawList(String name, RawList<?> value) {
		setRaw(name, value);
		return this;
	}

	default Data setByteList(String name, ByteList value) {
		setRawList(name, value);
		return this;
	}

	default Data setShortList(String name, ShortList value) {
		setRawList(name, value);
		return this;
	}

	default Data setIntList(String name, IntList value) {
		setRawList(name, value);
		return this;
	}

	default Data setLongList(String name, LongList value) {
		setRawList(name, value);
		return this;
	}

	default Data setFloatList(String name, FloatList value) {
		setRawList(name, value);
		return this;
	}

	default Data setDoubleList(String name, DoubleList value) {
		setRawList(name, value);
		return this;
	}

	default Data setByteArrayList(String name, ByteArrayList value) {
		setRawList(name, value);
		return this;
	}

	default Data setIntArrayList(String name, IntArrayList value) {
		setRawList(name, value);
		return this;
	}

	default Data setLongArrayList(String name, LongArrayList value) {
		setRawList(name, value);
		return this;
	}

	default Data setStringList(String name, StringList value) {
		setRawList(name, value);
		return this;
	}

	default Data setDataList(String name, DataList value) {
		setRawList(name, value);
		return this;
	}

	default Data setData(String name, Consumer<Data> consumer) {
		setData(name, Data.create(consumer));
		return this;
	}

	default Data setByteList(String name, byte... value) {
		setByteList(name, new ByteList(value));
		return this;
	}

	default Data setShortList(String name, short... value) {
		setShortList(name, new ShortList(value));
		return this;
	}

	default Data setIntList(String name, int... value) {
		setIntList(name, new IntList(value));
		return this;
	}

	default Data setLongList(String name, long... value) {
		setLongList(name, new LongList(value));
		return this;
	}

	default Data setFloatList(String name, float... value) {
		setFloatList(name, new FloatList(value));
		return this;
	}

	default Data setDoubleList(String name, double... value) {
		setDoubleList(name, new DoubleList(value));
		return this;
	}

	default Data setByteArrayList(String name, byte[]... value) {
		setByteArrayList(name, new ByteArrayList(value));
		return this;
	}

	default Data setIntArrayList(String name, int[]... value) {
		setIntArrayList(name, new IntArrayList(value));
		return this;
	}

	default Data setLongArrayList(String name, long[]... value) {
		setLongArrayList(name, new LongArrayList(value));
		return this;
	}

	default Data setStringList(String name, String... value) {
		setStringList(name, new StringList(value));
		return this;
	}

	default Data setDataList(String name, Data... value) {
		setDataList(name, new DataList(value));
		return this;
	}

	void writeBinary(File file);

}
