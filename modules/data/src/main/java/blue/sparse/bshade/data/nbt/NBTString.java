package blue.sparse.bshade.data.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NBTString extends NBTElement<NBTString.Type, String> {

	private String value;

	public NBTString(String value) {
		super(Type.INSTANCE);
		this.value = value;
	}

	@Override
	public String getRawValue() {
		return value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static class Type extends NBTType<NBTString> {

		public static final Type INSTANCE = new Type();

		private Type() {
			super(8);
		}

		@Override
		public void write(NBTString value, DataOutputStream out) throws IOException {
			out.writeUTF(value.value);
		}

		@Override
		public NBTString read(DataInputStream inp) throws IOException {
			return new NBTString(inp.readUTF());
		}
	}

}
