package blue.sparse.bshade.math.blocks.iterator;

import blue.sparse.bshade.math.blocks.util.BlockPosition;
import blue.sparse.bshade.math.blocks.section.BlockSection;

import java.util.NoSuchElementException;

public class RandomAccessSectionIterator implements BlockSectionIterator {

	private final BlockSection.RandomAccess region;
	private int index = 0;

	public RandomAccessSectionIterator(BlockSection.RandomAccess region) {
		this.region = region;
	}

	@Override
	public boolean hasNext() {
		return index < region.size();
	}

	@Override
	public BlockPosition next() {
		if(!hasNext())
			throw new NoSuchElementException();
		return region.get(index++);
	}
}
