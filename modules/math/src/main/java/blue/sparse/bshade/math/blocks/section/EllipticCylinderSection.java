package blue.sparse.bshade.math.blocks.section;

import blue.sparse.bshade.math.blocks.util.BlockPosition;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.UUID;

public class EllipticCylinderSection implements BlockSection {

	private final UUID worldID;
	private final BlockPosition minimum;
	private final BlockPosition maximum;

	public EllipticCylinderSection(World world, BlockPosition a, BlockPosition b) {
		this.worldID = world.getUID();

		this.minimum = a.min(b);
		this.maximum = a.max(b);
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
		BlockPosition size = getSize();
		return Math.PI * (size.getX() / 2.0) * (size.getZ() / 2.0) * size.getY();
	}

	@Override
	public UUID getWorldID() {
		return worldID;
	}

	@Override
	public boolean contains(double x, double y, double z) {
		if(y < minimum.getY() || y > maximum.getY() + 1)
			return false;

		Vector pos = square(getCenterExact().subtract(new Vector(x, y, z)))
				.divide(square(getSize().toVector().divide(new Vector(2, 2, 2))));

		return pos.getX() + pos.getZ() <= 1;
	}

	private static Vector square(Vector vector) {
		vector.setX(vector.getX() * vector.getX());
		vector.setY(vector.getY() * vector.getY());
		vector.setZ(vector.getZ() * vector.getZ());
		return vector;
	}
}
