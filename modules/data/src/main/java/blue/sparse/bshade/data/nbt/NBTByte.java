package blue.sparse.bshade.data.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NBTByte extends NBTElement<NBTByte.Type, Byte> {

	private byte value;

	public NBTByte(byte value) {
		super(Type.INSTANCE);
		this.value = value;
	}

	@Override
	public Byte getRawValue() {
		return value;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public static class Type extends NBTType<NBTByte> {

		public static final Type INSTANCE = new Type();

		private Type() {
			super(1);
		}

		@Override
		public void write(NBTByte value, DataOutputStream out) throws IOException {
			out.write(value.value);
		}

		@Override
		public NBTByte read(DataInputStream inp) throws IOException {
			return new NBTByte(inp.readByte());
		}
	}

}
