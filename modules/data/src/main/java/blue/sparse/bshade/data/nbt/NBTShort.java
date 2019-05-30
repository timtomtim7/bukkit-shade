package blue.sparse.bshade.data.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NBTShort extends NBTElement<NBTShort.Type, Short> {

	private short value;

	public NBTShort(short value) {
		super(Type.INSTANCE);
		this.value = value;
	}

	@Override
	public Short getRawValue() {
		return value;
	}

	public short getValue() {
		return value;
	}

	public void setValue(short value) {
		this.value = value;
	}

	public static class Type extends NBTType<NBTShort> {

		public static final Type INSTANCE = new Type();

		private Type() {
			super(2);
		}

		@Override
		public void write(NBTShort value, DataOutputStream out) throws IOException {
			out.writeShort(value.value);
		}

		@Override
		public NBTShort read(DataInputStream inp) throws IOException {
			return new NBTShort(inp.readShort());
		}
	}

}
