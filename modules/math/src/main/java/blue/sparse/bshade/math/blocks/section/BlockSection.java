package blue.sparse.bshade.math.blocks.section;

import blue.sparse.bshade.math.blocks.iterator.DefaultSectionIterator;
import blue.sparse.bshade.math.blocks.iterator.RandomAccessSectionIterator;
import blue.sparse.bshade.math.blocks.util.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.UUID;

public interface BlockSection extends Iterable<BlockPosition> {

	BlockPosition getMinimum();
	BlockPosition getMaximum();

	double getVolume();

	UUID getWorldID();

	default World getWorld() {
		return Bukkit.getWorld(getWorldID());
	}

	default boolean contains(BlockPosition position) {
		return contains(position.getWorldID(), position.getCenterX(), position.getCenterY(), position.getCenterZ());
	}

	default boolean contains(Vector vector) {
		return contains(vector.getX(), vector.getY(), vector.getZ());
	}

	default boolean contains(Location location) {
		return contains(location.getWorld(), location.getX(), location.getY(), location.getZ());
	}

	default boolean contains(UUID worldID, double x, double y, double z){
		if(worldID != null && !worldID.equals(getWorldID()))
			return false;

		return contains(x, y, z);
	}

	default boolean contains(World world, double x, double y, double z) {
		return contains(world == null ? null : world.getUID(), x, y, z);
	}

	boolean contains(double x, double y, double z);

	default BlockPosition getSize() {
		return getMaximum().plus(1).minus(getMinimum());
	}

	default BlockPosition getCenter() {
		return getSize().div(2).plus(getMinimum());
	}

	default Vector getCenterExact() {
		return getMaximum()
				.plus(1)
				.minus(getMinimum()).toVector()
				.divide(new Vector(2, 2, 2))
				.add(getMinimum().toVector());
	}

	@Override
	default Iterator<BlockPosition> iterator() {
		return new DefaultSectionIterator(this);
	}

	default HollowSection hollow() {
		return new HollowSection(this);
	}

	interface RandomAccess extends BlockSection {
		int size();

		BlockPosition get(int index);

		@Override
		default Iterator<BlockPosition> iterator() {
			return new RandomAccessSectionIterator(this);
		}
	}

}
