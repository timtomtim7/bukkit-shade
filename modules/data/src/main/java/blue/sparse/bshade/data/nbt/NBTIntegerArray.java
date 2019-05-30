package blue.sparse.bshade.data.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NBTIntegerArray extends NBTElement<NBTIntegerArray.Type, int[]> {

	private int[] value;

	public NBTIntegerArray(int[] value) {
		super(Type.INSTANCE);
		this.value = value;
	}

	@Override
	public int[] getRawValue() {
		return value;
	}

	public int[] getValue() {
		return value;
	}

	public void setValue(int[] value) {
		this.value = value;
	}

	public static class Type extends NBTType<NBTIntegerArray> {

		public static final Type INSTANCE = new Type();

		private Type() {
			super(11);
		}

		@Override
		public void write(NBTIntegerArray value, DataOutputStream out) throws IOException {
			out.writeInt(value.value.length);
			for (int i : value.value) {
				out.writeInt(i);
			}
		}

		@Override
		public NBTIntegerArray read(DataInputStream inp) throws IOException {
			int length = inp.readInt();
			int[] result = new int[length];

			for (int i = 0; i < result.length; i++) {
				result[i] = inp.readInt();
			}

			return new NBTIntegerArray(result);
		}
	}

}
