package blue.sparse.bshade.data.list;

import blue.sparse.bshade.data.Data;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

public class DataList implements RawList<Data> {
	private Data[] data;
	private int size;

	public DataList(Data... initial) {
		this.size = initial.length;
		this.data = Arrays.copyOf(initial, Math.max(size, 8));
	}

	public DataList(Collection<Data> collection) {
		this(collection.toArray(new Data[0]));
	}

	protected void checkBounds(int index) {
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	@Override
	public int size() {
		return size;
	}

	public Data get(int index) {
		checkBounds(index);
		return data[index];
	}

	public Data remove(int index) {
		checkBounds(index);

		Data removed = data[index];
		System.arraycopy(data, index + 1, data, index, data.length - index - 1);
		size--;

		return removed;
	}

	public Data set(int index, Data value) {
		if(index == size)
			add(size, value);

		checkBounds(index);

		Data old = data[index];
		data[index] = value;

		return old;
	}

	public void add(int index, Data value) {
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

	public void add(Data value) {
		add(size, value);
	}

	public void add(Consumer<Data> consumer) {
		add(Data.create(consumer));
	}

	@Override
	public Data getRaw(int index) {
		return get(index);
	}

	@Override
	public Data removeRaw(int index) {
		return remove(index);
	}

	@Override
	public Data setRaw(int index, Data value) {
		return set(index, value);
	}

	@Override
	public void addRaw(int index, Data value) {
		add(index, value);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append('[');
		for (int i = 0; i < size; i++) {
			Data value = data[i];
			if(result.length() != 1)
				result.append(", ");
			result.append(value);
		}
		result.append(']');

		return result.toString();
	}

	public Data[] toArray() {
		return Arrays.copyOf(data, size);
	}
}