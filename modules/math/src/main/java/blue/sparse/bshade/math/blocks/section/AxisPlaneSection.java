package blue.sparse.bshade.math.blocks.section;

import blue.sparse.bshade.math.blocks.util.Axis;
import blue.sparse.bshade.math.blocks.util.BlockPosition;
import org.bukkit.World;

public class AxisPlaneSection extends CuboidSection {

	private final Axis axis;

	public AxisPlaneSection(World world, BlockPosition a, BlockPosition b) {
		super(world, a, b);

		if (this.minimum.getX() == this.maximum.getX()) {
			this.axis = Axis.X;
		} else if (this.minimum.getY() == this.maximum.getY()) {
			this.axis = Axis.Y;
		} else if (this.minimum.getZ() == this.maximum.getZ()) {
			this.axis = Axis.Z;
		} else {
			throw new IllegalArgumentException("Points are not aligned in a plane");
		}
	}

	public Axis getAxis() {
		return axis;
	}

}
