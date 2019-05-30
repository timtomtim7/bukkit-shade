package blue.sparse.bshade.data.list;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Floats;

import java.util.Arrays;
import java.util.Collection;

public class ByteList implements RawList<Byte> {
	private byte[] data;
	private int size;

	public ByteList(byte... initial) {
		this.size = initial.length;
		this.data = Arrays.copyOf(initial, Math.max(size, 8));
	}

	public ByteList(Collection<Byte> collection) {
		this(Bytes.toArray(collection));
	}

	protected void checkBounds(int index) {
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	@Override
	public int size() {
		return size;
	}

	public byte get(int index) {
		checkBounds(index);
		return data[index];
	}

	public byte remove(int index) {
		checkBounds(index);

		byte removed = data[index];
		System.arraycopy(data, index + 1, data, index, data.length - index - 1);
		size--;

		return removed;
	}

	public byte set(int index, byte value) {
		if(index == size)
			add(size, value);

		checkBounds(index);

		byte old = data[index];
		data[index] = value;

		return old;
	}

	public void add(int index, byte value) {
		if(data.length < size) {
			increaseSize();
		}

		System.arraycopy(data, index, data, index + 1, data.length - index - 1);
		data[index] = value;
		size++;
	}

	private void increaseSize() {
		this.data = Arrays.copyOf(data, data.length + Math.max(8, data.length / 16));
	}

	public void add(byte value) {
		add(size, value);
	}

	@Override
	public Byte getRaw(int index) {
		return get(index);
	}

	@Override
	public Byte removeRaw(int index) {
		return remove(index);
	}

	@Override
	public Byte setRaw(int index, Byte value) {
		return set(index, value);
	}

	@Override
	public void addRaw(int index, Byte value) {
		add(index, value);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append('[');
		for (int i = 0; i < size; i++) {
			byte value = data[i];
			if(result.length() != 1)
				result.append(", ");
			result.append(value);
		}
		result.append(']');

		return result.toString();
	}

	public byte[] toArray() {
		return Arrays.copyOf(data, size);
	}
}