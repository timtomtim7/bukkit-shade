package blue.sparse.bshade.data;

import blue.sparse.bshade.data.list.*;
import blue.sparse.bshade.data.nbt.NBTCompound;

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

	Object getRawOrNull(String name);

	default Object getRaw(String name) {
		final Object raw = getRawOrNull(name);
		if(raw == null)
			throw new NoSuchElementException(name);

		return raw;
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


	void setRaw(String name, Object value);

	default void setByte(String name, byte value) {
		setRaw(name, value);
	}

	default void setShort(String name, short value) {
		setRaw(name, value);
	}

	default void setInt(String name, int value) {
		setRaw(name, value);
	}

	default void setLong(String name, long value) {
		setRaw(name, value);
	}

	default void setFloat(String name, float value) {
		setRaw(name, value);
	}

	default void setDouble(String name, double value) {
		setRaw(name, value);
	}

	default void setByteArray(String name, byte... value) {
		setRaw(name, value);
	}

	default void setIntArray(String name, int... value) {
		setRaw(name, value);
	}

	default void setLongArray(String name, long... value) {
		setRaw(name, value);
	}

	default void setString(String name, String value) {
		setRaw(name, value);
	}

	default void setData(String name, Data value) {
		setRaw(name, value);
	}


	default void setRawList(String name, RawList<?> value) {
		setRaw(name, value);
	}

	default void setByteList(String name, ByteList value) {
		setRawList(name, value);
	}

	default void setShortList(String name, ShortList value) {
		setRawList(name, value);
	}

	default void setIntList(String name, IntList value) {
		setRawList(name, value);
	}

	default void setLongList(String name, LongList value) {
		setRawList(name, value);
	}

	default void setFloatList(String name, FloatList value) {
		setRawList(name, value);
	}

	default void setDoubleList(String name, DoubleList value) {
		setRawList(name, value);
	}

	default void setByteArrayList(String name, ByteArrayList value) {
		setRawList(name, value);
	}

	default void setIntArrayList(String name, IntArrayList value) {
		setRawList(name, value);
	}

	default void setLongArrayList(String name, LongArrayList value) {
		setRawList(name, value);
	}

	default void setStringList(String name, StringList value) {
		setRawList(name, value);
	}

	default void setDataList(String name, DataList value) {
		setRawList(name, value);
	}

	default void setData(String name, Consumer<Data> consumer) {
		setData(name, Data.create(consumer));
	}

	default void setByteList(String name, byte... value) {
		setByteList(name, new ByteList(value));
	}

	default void setShortList(String name, short... value) {
		setShortList(name, new ShortList(value));
	}

	default void setIntList(String name, int... value) {
		setIntList(name, new IntList(value));
	}

	default void setLongList(String name, long... value) {
		setLongList(name, new LongList(value));
	}

	default void setFloatList(String name, float... value) {
		setFloatList(name, new FloatList(value));
	}

	default void setDoubleList(String name, double... value) {
		setDoubleList(name, new DoubleList(value));
	}

	default void setByteArrayList(String name, byte[]... value) {
		setByteArrayList(name, new ByteArrayList(value));
	}

	default void setIntArrayList(String name, int[]... value) {
		setIntArrayList(name, new IntArrayList(value));
	}

	default void setLongArrayList(String name, long[]... value) {
		setLongArrayList(name, new LongArrayList(value));
	}

	default void setStringList(String name, String... value) {
		setStringList(name, new StringList(value));
	}

	default void setDataList(String name, Data... value) {
		setDataList(name, new DataList(value));
	}

	void writeBinary(File file);

}
