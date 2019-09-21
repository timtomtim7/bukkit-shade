package blue.sparse.bshade.math.blocks.section;

import blue.sparse.bshade.math.blocks.util.Axis;
import blue.sparse.bshade.math.blocks.util.BlockPosition;
import com.google.common.base.Strings;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.World;

import java.util.*;

public class CuboidSection implements BlockSection.RandomAccess {

	protected final UUID worldID;
	protected final BlockPosition minimum;
	protected final BlockPosition maximum;

	public CuboidSection(World world, BlockPosition a, BlockPosition b) {
		this.worldID = world.getUID();

		this.minimum = a.min(b);
		this.maximum = a.max(b);
	}

	public List<AxisPlaneSection> layers(Axis axis) {
		List<AxisPlaneSection> result = new ArrayList<>();
		int count = getSize().get(axis);

		int min = minimum.get(axis);
		for (int i = 0; i < count; i++) {
			AxisPlaneSection plane = new AxisPlaneSection(
					getWorld(),
					this.minimum.with(axis, min + i),
					this.maximum.with(axis, min + i)
			);
			result.add(plane);
		}

		return result;
	}

	@Override
	public BlockPosition getMinimum() {
		return minimum;
	}

	@Override
	public BlockPosition getMaximum() {
		return maximum;
	}

	@Override
	public double getVolume() {
		return size();
	}

	@Override
	public UUID getWorldID() {
		return worldID;
	}

	@Override
	public boolean contains(double x, double y, double z) {
		return x >= this.minimum.getX()
				&& y >= this.minimum.getY()
				&& z >= this.minimum.getZ()
				&& x <= this.maximum.getX() + 1
				&& y <= this.maximum.getY() + 1
				&& z <= this.maximum.getZ() + 1;
	}

	@Override
	public int size() {
		BlockPosition size = getSize();
		return size.getX() * size.getY() * size.getZ();
	}

	@Override
	public BlockPosition get(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException(String.valueOf(index));

		BlockPosition size = getSize();

		int x = index % size.getX();
		int y = (index / size.getX()) % size.getY();
		int z = ((index / size.getX()) / size.getY()) % size.getZ();

		return new BlockPosition(worldID, x, y, z).plus(minimum);
	}

}
