package blue.sparse.bshade.math.blocks.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.UUID;

public class BlockPosition {

	private final UUID worldID;
	private final int x;
	private final int y;
	private final int z;

	public BlockPosition(UUID worldID, int x, int y, int z) {
		this.worldID = worldID;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockPosition(World world, int x, int y, int z) {
		this(world.getUID(), x, y, z);
	}

	public BlockPosition(Block block) {
		this(block.getWorld(), block.getX(), block.getY(), block.getZ());
	}

	public BlockPosition(Location location) {
		this(location.getBlock()); // TODO: Make more efficient?
	}

	public void set(Material material, boolean update) {
		toBlock().setType(material, update);
	}

	public void set(Material material) {
		set(material, true);
	}

	public void setLegacy(Material material, byte data) {
		setLegacy(material, data, true);
	}

	public void setLegacy(Material material, byte data, boolean update) {
		toBlock().setTypeIdAndData(material.getId(), data, update);
	}


	public int get(Axis axis) {
		switch (axis) {
			case X: return this.x;
			case Y: return this.y;
			case Z: return this.z;
		}

		throw new IllegalStateException();
	}

	public BlockPosition with(Axis axis, int value) {
		switch (axis) {
			case X: return withX(value);
			case Y: return withY(value);
			case Z: return withZ(value);
		}

		throw new IllegalStateException();
	}

	public BlockPosition withX(int x) {
		return new BlockPosition(this.worldID, x, this.y, this.z);
	}

	public BlockPosition withY(int y) {
		return new BlockPosition(this.worldID, this.x, y, this.z);
	}

	public BlockPosition withZ(int z) {
		return new BlockPosition(this.worldID, this.x, this.y, z);
	}

	public double distance(BlockPosition other) {
		return Math.sqrt(distanceSquared(other));
	}

	public double distanceSquared(BlockPosition other) {
		double dx = getCenterX() - other.getCenterX();
		double dy = getCenterY() - other.getCenterY();
		double dz = getCenterZ() - other.getCenterZ();
		return dx * dx + dy * dy + dz * dz;
	}

	public int manhattanDistance(BlockPosition other) {
		return minus(other).abs().sum();
	}

	public double getCenterX() {
		return x + 0.5;
	}

	public double getCenterY() {
		return y + 0.5;
	}

	public double getCenterZ() {
		return z + 0.5;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public World getWorld() {
		return Bukkit.getWorld(worldID);
	}

	public UUID getWorldID() {
		return worldID;
	}

	public BlockPosition plus(BlockPosition other) {
		return plus(other.x, other.y, other.z);
	}

	public BlockPosition plus(int all) {
		return plus(all, all, all);
	}

	public BlockPosition plus(int x, int y, int z) {
		return new BlockPosition(worldID, this.x + x, this.y + y, this.z + z);
	}

	public BlockPosition minus(BlockPosition other) {
		return minus(other.x, other.y, other.z);
	}

	public BlockPosition minus(int all) {
		return minus(all, all, all);
	}

	public BlockPosition minus(int x, int y, int z) {
		return new BlockPosition(worldID, this.x - x, this.y - y, this.z - z);
	}

	public BlockPosition times(BlockPosition other) {
		return times(other.x, other.y, other.z);
	}

	public BlockPosition times(int all) {
		return times(all, all, all);
	}

	public BlockPosition times(int x, int y, int z) {
		return new BlockPosition(worldID, this.x * x, this.y * y, this.z * z);
	}

	public BlockPosition div(BlockPosition other) {
		return div(other.x, other.y, other.z);
	}

	public BlockPosition div(int all) {
		return div(all, all, all);
	}

	public BlockPosition div(int x, int y, int z) {
		return new BlockPosition(worldID, this.x / x, this.y / y, this.z / z);
	}

	public BlockPosition rem(BlockPosition other) {
		return rem(other.x, other.y, other.z);
	}

	public BlockPosition rem(int all) {
		return rem(all, all, all);
	}

	public BlockPosition rem(int x, int y, int z) {
		return new BlockPosition(worldID, this.x % x, this.y % y, this.z % z);
	}

	public BlockPosition abs() {
		return new BlockPosition(worldID, Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
	}

	public BlockPosition max(BlockPosition other) {
		return new BlockPosition(worldID, Math.max(this.x, other.x), Math.max(this.y, other.y), Math.max(this.z, other.z));
	}

	public BlockPosition min(BlockPosition other) {
		return new BlockPosition(worldID, Math.min(this.x, other.x), Math.min(this.y, other.y), Math.min(this.z, other.z));
	}

	public int sum() {
		return this.x + this.y + this.z;
	}

	public Location toCenterLocation() {
		return new Location(getWorld(), getCenterX(), getCenterY(), getCenterZ());
	}

	public Location toLocation() {
		return new Location(getWorld(), this.x, this.y, this.z);
	}

	public Vector toCenterVector() {
		return new Vector(getCenterX(), getCenterY(), getCenterZ());
	}

	public Vector toVector() {
		return new Vector(this.x, this.y, this.z);
	}

	public BlockVector toBlockVector() {
		return new BlockVector(this.x, this.y, this.z);
	}

	public Block toBlock() {
		return getWorld().getBlockAt(this.x, this.y, this.z);
	}

	public boolean isLoaded() {
		World world = getWorld();
		if(world == null)
			return false;

		return world.isChunkLoaded(this.x >> 4, this.z >> 4);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BlockPosition)) return false;
		BlockPosition that = (BlockPosition) o;
		return x == that.x &&
				y == that.y &&
				z == that.z &&
				Objects.equals(worldID, that.worldID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(worldID, x, y, z);
	}

	@Override
	public String toString() {
		return "BlockPosition{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				'}';
	}
}
