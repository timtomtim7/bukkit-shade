package blue.sparse.bshade.data.list;

import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.Collection;

public class FloatList implements RawList<Float> {
	private float[] data;
	private int size;

	public FloatList(float... initial) {
		this.size = initial.length;
		this.data = Arrays.copyOf(initial, Math.max(size, 8));
	}

	public FloatList(Collection<Float> collection) {
		this(Floats.toArray(collection));
	}

	protected void checkBounds(int index) {
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	@Override
	public int size() {
		return size;
	}

	public float get(int index) {
		checkBounds(index);
		return data[index];
	}

	public float remove(int index) {
		checkBounds(index);

		float removed = data[index];
		System.arraycopy(data, index + 1, data, index, data.length - index - 1);
		size--;

		return removed;
	}

	public float set(int index, float value) {
		if(index == size)
			add(size, value);

		checkBounds(index);

		float old = data[index];
		data[index] = value;

		return old;
	}

	public void add(int index, float value) {
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

	public void add(float value) {
		add(size, value);
	}

	@Override
	public Float getRaw(int index) {
		return get(index);
	}

	@Override
	public Float removeRaw(int index) {
		return remove(index);
	}

	@Override
	public Float setRaw(int index, Float value) {
		return set(index, value);
	}

	@Override
	public void addRaw(int index, Float value) {
		add(index, value);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append('[');
		for (int i = 0; i < size; i++) {
			float value = data[i];
			if(result.length() != 1)
				result.append(", ");
			result.append(value);
		}
		result.append(']');

		return result.toString();
	}

	public float[] toArray() {
		return Arrays.copyOf(data, size);
	}
}