package blue.sparse.bshade.data.nbt;

public abstract class NBTElement<T extends NBTType<?>, V> {

	public final T type;

	public NBTElement(T type) {
		this.type = type;
	}

	public abstract V getRawValue();

}
