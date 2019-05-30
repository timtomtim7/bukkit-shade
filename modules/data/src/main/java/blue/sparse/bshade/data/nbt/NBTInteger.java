package blue.sparse.bshade.data.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NBTInteger extends NBTElement<NBTInteger.Type, Integer> {

	private int value;

	public NBTInteger(int value) {
		super(Type.INSTANCE);
		this.value = value;
	}

	@Override
	public Integer getRawValue() {
		return value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static class Type extends NBTType<NBTInteger> {

		public static final Type INSTANCE = new Type();

		private Type() {
			super(3);
		}

		@Override
		public void write(NBTInteger value, DataOutputStream out) throws IOException {
			out.writeInt(value.value);
		}

		@Override
		public NBTInteger read(DataInputStream inp) throws IOException {
			return new NBTInteger(inp.readInt());
		}
	}

}
