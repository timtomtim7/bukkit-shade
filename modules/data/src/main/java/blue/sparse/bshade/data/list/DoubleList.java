package blue.sparse.bshade.data.list;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.Collection;

public class DoubleList implements RawList<Double> {
	private double[] data;
	private int size;

	public DoubleList(double... initial) {
		this.size = initial.length;
		this.data = Arrays.copyOf(initial, Math.max(size, 8));
	}

	public DoubleList(Collection<Double> collection) {
		this(Doubles.toArray(collection));
	}

	protected void checkBounds(int index) {
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	@Override
	public int size() {
		return size;
	}

	public double get(int index) {
		checkBounds(index);
		return data[index];
	}

	public double remove(int index) {
		checkBounds(index);

		double removed = data[index];
		System.arraycopy(data, index + 1, data, index, data.length - index - 1);
		size--;

		return removed;
	}

	public double set(int index, double value) {
		if(index == size)
			add(size, value);

		checkBounds(index);

		double old = data[index];
		data[index] = value;

		return old;
	}

	public void add(int index, double value) {
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

	public void add(double value) {
		add(size, value);
	}

	@Override
	public Double getRaw(int index) {
		return get(index);
	}

	@Override
	public Double removeRaw(int index) {
		return remove(index);
	}

	@Override
	public Double setRaw(int index, Double value) {
		return set(index, value);
	}

	@Override
	public void addRaw(int index, Double value) {
		add(index, value);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append('[');
		for (int i = 0; i < size; i++) {
			double value = data[i];
			if(result.length() != 1)
				result.append(", ");
			result.append(value);
		}
		result.append(']');

		return result.toString();
	}

	public double[] toArray() {
		return Arrays.copyOf(data, size);
	}
}