package blue.sparse.bshade.data.list;

import com.google.common.primitives.Longs;

import java.util.Arrays;
import java.util.Collection;

public class LongList implements RawList<Long> {
	private long[] data;
	private int size;

	public LongList(long... initial) {
		this.size = initial.length;
		this.data = Arrays.copyOf(initial, Math.max(size, 8));
	}

	public LongList(Collection<Long> collection) {
		this(Longs.toArray(collection));
	}

	protected void checkBounds(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	@Override
	public int size() {
		return size;
	}

	public long get(int index) {
		checkBounds(index);
		return data[index];
	}

	public long remove(int index) {
		checkBounds(index);

		long removed = data[index];
		System.arraycopy(data, index + 1, data, index, data.length - index - 1);
		size--;

		return removed;
	}

	public long set(int index, long value) {
		if (index == size)
			add(size, value);

		checkBounds(index);

		long old = data[index];
		data[index] = value;

		return old;
	}

	public void add(int index, long value) {
		if (data.length < size) {
			increaseSize();
		}

		System.arraycopy(data, index, data, index + 1, data.length - index - 1);
		data[index] = value;
		size++;
	}

	private void increaseSize() {
		this.data = Arrays.copyOf(data, data.length + Math.max(8, data.length / 16));
	}

	public void add(long value) {
		add(size, value);
	}

	@Override
	public Long getRaw(int index) {
		return get(index);
	}

	@Override
	public Long removeRaw(int index) {
		return remove(index);
	}

	@Override
	public Long setRaw(int index, Long value) {
		return set(index, value);
	}

	@Override
	public void addRaw(int index, Long value) {
		add(index, value);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append('[');
		for (int i = 0; i < size; i++) {
			long value = data[i];
			if (result.length() != 1)
				result.append(", ");
			result.append(value);
		}
		result.append(']');

		return result.toString();
	}

	public long[] toArray() {
		return Arrays.copyOf(data, size);
	}
}