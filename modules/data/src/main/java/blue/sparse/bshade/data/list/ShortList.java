package blue.sparse.bshade.data.list;

import com.google.common.primitives.Shorts;

import java.util.Arrays;
import java.util.Collection;

public class ShortList implements RawList<Short> {
	private short[] data;
	private int size;

	public ShortList(short... initial) {
		this.size = initial.length;
		this.data = Arrays.copyOf(initial, Math.max(size, 8));
	}

	public ShortList(Collection<Short> collection) {
		this(Shorts.toArray(collection));
	}

	protected void checkBounds(int index) {
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	@Override
	public int size() {
		return size;
	}

	public short get(int index) {
		checkBounds(index);
		return data[index];
	}

	public short remove(int index) {
		checkBounds(index);

		short removed = data[index];
		System.arraycopy(data, index + 1, data, index, data.length - index - 1);
		size--;

		return removed;
	}

	public short set(int index, short value) {
		if(index == size)
			add(size, value);

		checkBounds(index);

		short old = data[index];
		data[index] = value;

		return old;
	}

	public void add(int index, short value) {
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

	public void add(short value) {
		add(size, value);
	}

	@Override
	public Short getRaw(int index) {
		return get(index);
	}

	@Override
	public Short removeRaw(int index) {
		return remove(index);
	}

	@Override
	public Short setRaw(int index, Short value) {
		return set(index, value);
	}

	@Override
	public void addRaw(int index, Short value) {
		add(index, value);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append('[');
		for (int i = 0; i < size; i++) {
			short value = data[i];
			if(result.length() != 1)
				result.append(", ");
			result.append(value);
		}
		result.append(']');

		return result.toString();
	}

	public short[] toArray() {
		return Arrays.copyOf(data, size);
	}
}