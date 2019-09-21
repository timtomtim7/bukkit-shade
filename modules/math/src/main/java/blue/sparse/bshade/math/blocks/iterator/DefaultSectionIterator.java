package blue.sparse.bshade.math.blocks.iterator;

import blue.sparse.bshade.math.blocks.section.BlockSection;
import blue.sparse.bshade.math.blocks.section.CuboidSection;
import blue.sparse.bshade.math.blocks.util.BlockPosition;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DefaultSectionIterator implements BlockSectionIterator {

	private final BlockSection section;
	private final Iterator<BlockPosition> cuboidIterator;

	private BlockPosition next;

	public DefaultSectionIterator(BlockSection section) {
		this.section = section;
		this.cuboidIterator = new CuboidSection(section.getWorld(), section.getMinimum(), section.getMaximum()).iterator();
	}

	private void findNext() {
		do {
			if(!cuboidIterator.hasNext()) {
				next = null;
				break;
			}

			next = cuboidIterator.next();
		} while(!section.contains(next));
	}

	@Override
	public boolean hasNext() {
		if(this.next == null) {
			findNext();
		}
		return this.next != null;
	}

	@Override
	public BlockPosition next() {
		if(this.next == null)
			findNext();
		if(this.next == null)
			throw new NoSuchElementException();

		BlockPosition result = this.next;
		this.next = null;
		return result;
	}
}
