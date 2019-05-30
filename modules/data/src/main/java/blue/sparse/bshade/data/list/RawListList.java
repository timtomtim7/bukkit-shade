package blue.sparse.bshade.data.list;

import com.google.common.primitives.Shorts;

import java.util.Arrays;
import java.util.Collection;

public class RawListList implements RawList<RawList<?>> {
	private RawList<?>[] data;
	private int size;

	public RawListList(RawList<?>... initial) {
		this.size = initial.length;
		this.data = Arrays.copyOf(initial, Math.max(size, 8));
	}

	public RawListList(Collection<RawList<?>> collection) {
		this(collection.toArray(new RawList<?>[0]));
	}

	protected void checkBounds(int index) {
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	@Override
	public int size() {
		return size;
	}

	public RawList<?> get(int index) {
		checkBounds(index);
		return data[index];
	}

	public RawList<?> remove(int index) {
		checkBounds(index);

		RawList<?> removed = data[index];
		System.arraycopy(data, index + 1, data, index, data.length - index - 1);
		size--;

		return removed;
	}

	public RawList<?> set(int index, RawList<?> value) {
		if(index == size)
			add(size, value);

		checkBounds(index);

		RawList<?> old = data[index];
		data[index] = value;

		return old;
	}

	public void add(int index, RawList<?> value) {
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

	public void add(RawList<?> value) {
		add(size, value);
	}

	@Override
	public RawList<?> getRaw(int index) {
		return get(index);
	}

	@Override
	public RawList<?> removeRaw(int index) {
		return remove(index);
	}

	@Override
	public RawList<?> setRaw(int index, RawList<?> value) {
		return set(index, value);
	}

	@Override
	public void addRaw(int index, RawList<?> value) {
		add(index, value);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append('[');
		for (int i = 0; i < size; i++) {
			RawList<?> value = data[i];
			if(result.length() != 1)
				result.append(", ");
			result.append(value);
		}
		result.append(']');

		return result.toString();
	}

	public RawList<?>[] toArray() {
		return Arrays.copyOf(data, size);
	}
}