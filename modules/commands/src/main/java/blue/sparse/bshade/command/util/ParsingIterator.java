package blue.sparse.bshade.command.util;

import java.util.NoSuchElementException;

public class ParsingIterator implements Cloneable {

	public final String value;

	private int index;

	public ParsingIterator(String value) {
		this.value = value;
	}

	public String takeWhile(CharPredicate predicate) {
		return takeUntil(c -> !predicate.apply(c));
	}

	public String takeUntil(CharPredicate predicate) {
		final StringBuilder result = new StringBuilder();

		while(hasNext()) {
			final char c = next();
			if(predicate.apply(c)) {
				previous();
				break;
			}

			result.append(c);
		}

		return result.toString();
	}

	public boolean hasNext() {
		return index < value.length();
	}

	public boolean hasPrevious() {
		return index > 0;
	}

	public char next() {
		if(!hasNext())
			throw new NoSuchElementException();

		return value.charAt(index++);
	}

	public char previous() {
		if(!hasPrevious())
			throw new NoSuchElementException();

		return value.charAt(--index);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public ParsingIterator clone(){
		try {
			return (ParsingIterator) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return null;
	}
}
