package blue.sparse.bshade.data.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NBTFloat extends NBTElement<NBTFloat.Type, Float> {

	private float value;

	public NBTFloat(float value) {
		super(Type.INSTANCE);
		this.value = value;
	}

	@Override
	public Float getRawValue() {
		return value;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public static class Type extends NBTType<NBTFloat> {

		public static final Type INSTANCE = new Type();

		private Type() {
			super(5);
		}

		@Override
		public void write(NBTFloat value, DataOutputStream out) throws IOException {
			out.writeFloat(value.value);
		}

		@Override
		public NBTFloat read(DataInputStream inp) throws IOException {
			return new NBTFloat(inp.readFloat());
		}
	}

}
