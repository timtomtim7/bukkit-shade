package blue.sparse.bshade.data.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NBTDouble extends NBTElement<NBTDouble.Type, Double> {

	private double value;

	public NBTDouble(double value) {
		super(Type.INSTANCE);
		this.value = value;
	}

	@Override
	public Double getRawValue() {
		return value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public static class Type extends NBTType<NBTDouble> {

		public static final Type INSTANCE = new Type();

		private Type() {
			super(6);
		}

		@Override
		public void write(NBTDouble value, DataOutputStream out) throws IOException {
			out.writeDouble(value.value);
		}

		@Override
		public NBTDouble read(DataInputStream inp) throws IOException {
			return new NBTDouble(inp.readDouble());
		}
	}

}
