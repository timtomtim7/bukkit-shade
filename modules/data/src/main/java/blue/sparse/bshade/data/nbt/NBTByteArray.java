package blue.sparse.bshade.data.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NBTByteArray extends NBTElement<NBTByteArray.Type, byte[]> {

	private byte[] value;

	public NBTByteArray(byte[] value) {
		super(Type.INSTANCE);
		this.value = value;
	}

	@Override
	public byte[] getRawValue() {
		return value;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public static class Type extends NBTType<NBTByteArray> {

		public static final Type INSTANCE = new Type();

		private Type() {
			super(7);
		}

		@Override
		public void write(NBTByteArray value, DataOutputStream out) throws IOException {
			out.writeInt(value.value.length);
			out.write(value.value);
		}

		@Override
		public NBTByteArray read(DataInputStream inp) throws IOException {
			int length = inp.readInt();
			byte[] result = new byte[length];
			inp.readFully(result);
			return new NBTByteArray(result);
		}
	}

}
