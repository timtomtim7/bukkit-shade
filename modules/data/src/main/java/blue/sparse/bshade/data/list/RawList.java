package blue.sparse.bshade.data.list;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public interface RawList<T> extends Iterable<T> {

	int size();

	T getRaw(int index);

	T removeRaw(int index);
	T setRaw(int index, T value);
	void addRaw(int index, T value);

	default void addRaw(T value) {
		addRaw(size(), value);
	}

	default List<T> toList() {
		List<T> result = new ArrayList<>();
		int size = size();
		for (int i = 0; i < size; i++)
			result.add(getRaw(i));
		return result;
	}

	@Override
	default ListIterator<T> iterator() {
		return new ListIterator<T>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < size();
			}

			@Override
			public T next() {
				return getRaw(index++);
			}

			@Override
			public boolean hasPrevious() {
				return index > 0;
			}

			@Override
			public T previous() {
				return getRaw(index--);
			}

			@Override
			public int nextIndex() {
				return index;
			}

			@Override
			public int previousIndex() {
				return index - 1;
			}

			@Override
			public void remove() {
				removeRaw(index);
			}

			@Override
			public void set(T t) {
				setRaw(index, t);
			}

			@Override
			public void add(T t) {
				addRaw(t);
			}

		};
	}
}
