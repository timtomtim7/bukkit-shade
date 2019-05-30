package blue.sparse.bshade.data.list;

import java.util.Arrays;
import java.util.Collection;

public class StringList implements RawList<String> {
	private String[] data;
	private int size;

	public StringList(String... initial) {
		this.size = initial.length;
		this.data = Arrays.copyOf(initial, Math.max(size, 8));
	}

	public StringList(Collection<String> collection) {
		this(collection.toArray(new String[0]));
	}

	protected void checkBounds(int index) {
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	@Override
	public int size() {
		return size;
	}

	public String get(int index) {
		checkBounds(index);
		return data[index];
	}

	public String remove(int index) {
		checkBounds(index);

		String removed = data[index];
		System.arraycopy(data, index + 1, data, index, data.length - index - 1);
		size--;

		return removed;
	}

	public String set(int index, String value) {
		if(index == size)
			add(size, value);

		checkBounds(index);

		String old = data[index];
		data[index] = value;

		return old;
	}

	public void add(int index, String value) {
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

	public void add(String value) {
		add(size, value);
	}

	@Override
	public String getRaw(int index) {
		return get(index);
	}

	@Override
	public String removeRaw(int index) {
		return remove(index);
	}

	@Override
	public String setRaw(int index, String value) {
		return set(index, value);
	}

	@Override
	public void addRaw(int index, String value) {
		add(index, value);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append('[');
		for (int i = 0; i < size; i++) {
			String value = data[i];
			if(result.length() != 1)
				result.append(", ");
			result.append(value);
		}
		result.append(']');

		return result.toString();
	}

	public String[] toArray() {
		return Arrays.copyOf(data, size);
	}
}