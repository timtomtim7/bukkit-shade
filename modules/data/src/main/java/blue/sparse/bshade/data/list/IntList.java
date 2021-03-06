package blue.sparse.bshade.data.list;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;

import java.util.Arrays;
import java.util.Collection;

public class IntList implements RawList<Integer> {
	private int[] data;
	private int size;

	public IntList(int... initial) {
		this.size = initial.length;
		this.data = Arrays.copyOf(initial, Math.max(size, 8));
	}

	public IntList(Collection<Integer> collection) {
		this(Ints.toArray(collection));
	}

	protected void checkBounds(int index) {
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	@Override
	public int size() {
		return size;
	}

	public int get(int index) {
		checkBounds(index);
		return data[index];
	}

	public int remove(int index) {
		checkBounds(index);

		int removed = data[index];
		System.arraycopy(data, index + 1, data, index, data.length - index - 1);
		size--;

		return removed;
	}

	public int set(int index, int value) {
		if(index == size)
			add(size, value);

		checkBounds(index);

		int old = data[index];
		data[index] = value;

		return old;
	}

	public void add(int index, int value) {
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

	public void add(int value) {
		add(size, value);
	}

	@Override
	public Integer getRaw(int index) {
		return get(index);
	}

	@Override
	public Integer removeRaw(int index) {
		return remove(index);
	}

	@Override
	public Integer setRaw(int index, Integer value) {
		return set(index, value);
	}

	@Override
	public void addRaw(int index, Integer value) {
		add(index, value);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append('[');
		for (int i = 0; i < size; i++) {
			int value = data[i];
			if(result.length() != 1)
				result.append(", ");
			result.append(value);
		}
		result.append(']');

		return result.toString();
	}

	public int[] toArray() {
		return Arrays.copyOf(data, size);
	}
}