package blue.sparse.bshade.math.blocks.sort;

import blue.sparse.bshade.math.blocks.util.BlockPosition;
import blue.sparse.bshade.math.blocks.section.BlockSection;

import java.util.Comparator;

public interface BlockSorter {

	int compare(BlockSection section, BlockPosition a, BlockPosition b);

	default Comparator<BlockPosition> comparator(BlockSection section) {
		return (a, b) -> compare(section, a, b);
	}

	BlockSorter DISTANCE = (section, a, b) -> {
		BlockPosition center = section.getCenter();
		return Double.compare(
				a.distanceSquared(center),
				b.distanceSquared(center)
		);
	};

	BlockSorter MANHATTAN_DISTANCE = (section, a, b) -> {
		BlockPosition center = section.getCenter();
		return Integer.compare(
				a.manhattanDistance(center),
				b.manhattanDistance(center)
		);
	};

}
