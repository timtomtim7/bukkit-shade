package blue.sparse.bshade.data.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NBTLongArray extends NBTElement<NBTLongArray.Type, long[]> {

	private long[] value;

	public NBTLongArray(long[] value) {
		super(Type.INSTANCE);
		this.value = value;
	}

	@Override
	public long[] getRawValue() {
		return value;
	}

	public long[] getValue() {
		return value;
	}

	public void setValue(long[] value) {
		this.value = value;
	}

	public static class Type extends NBTType<NBTLongArray> {

		public static final Type INSTANCE = new Type();

		private Type() {
			super(12);
		}

		@Override
		public void write(NBTLongArray value, DataOutputStream out) throws IOException {
			out.writeInt(value.value.length);
			for (long l : value.value) {
				out.writeLong(l);
			}
		}

		@Override
		public NBTLongArray read(DataInputStream inp) throws IOException {
			int length = inp.readInt();
			long[] result = new long[length];

			for (int i = 0; i < length; i++) {
				result[i] = inp.readLong();
			}

			return new NBTLongArray(result);
		}
	}

}
