package blue.sparse.bshade.math.blocks.section;

import blue.sparse.bshade.math.blocks.util.Axis;
import blue.sparse.bshade.math.blocks.util.BlockPosition;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class HollowSection implements BlockSection {

	protected final BlockSection section;
	protected final int threshold;
	protected final Set<Axis> axes;

	public HollowSection(BlockSection section, int threshold, Axis... ignoreAxes) {
		this.section = section;
		this.threshold = threshold;
		this.axes = EnumSet.allOf(Axis.class);
		this.axes.removeAll(Arrays.asList(ignoreAxes));
	}

	public HollowSection(BlockSection section) {
		this(section, 1);
	}

	public int getThreshold() {
		return threshold;
	}

	@Override
	public BlockPosition getMinimum() {
		return section.getMinimum();
	}

	@Override
	public BlockPosition getMaximum() {
		return section.getMaximum();
	}

	@Override
	public double getVolume() {
		return section.getVolume(); // TODO ?
	}

	@Override
	public UUID getWorldID() {
		return section.getWorldID();
	}

	public HollowSection withThreshold(int threshold) {
		return new HollowSection(this.section, threshold);
	}

	@Override
	public boolean contains(double x, double y, double z) {
		if (!section.contains(x, y, z))
			return false;

		int count = 0;

		for (Axis axis : axes) {
			if(!section.contains(x + axis.getX(), y + axis.getY(), z + axis.getZ()))
				count++;

			if(!section.contains(x - axis.getX(), y - axis.getY(), z - axis.getZ()))
				count++;

			if(count >= threshold)
				return true;
		}

		return count >= threshold;
	}
}
