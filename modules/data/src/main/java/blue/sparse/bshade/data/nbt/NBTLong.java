package blue.sparse.bshade.data.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NBTLong extends NBTElement<NBTLong.Type, Long> {

	private long value;

	public NBTLong(long value) {
		super(Type.INSTANCE);
		this.value = value;
	}

	@Override
	public Long getRawValue() {
		return value;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public static class Type extends NBTType<NBTLong> {

		public static final Type INSTANCE = new Type();

		private Type() {
			super(4);
		}

		@Override
		public void write(NBTLong value, DataOutputStream out) throws IOException {
			out.writeLong(value.value);
		}

		@Override
		public NBTLong read(DataInputStream inp) throws IOException {
			return new NBTLong(inp.readLong());
		}
	}

}
