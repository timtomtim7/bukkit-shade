package blue.sparse.bshade.data.nbt;

import blue.sparse.bshade.data.Data;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NBTCompound extends NBTElement<NBTCompound.Type, Data> implements Data {

	private Map<String, NBTElement<?, ?>> value;

	public NBTCompound() {
		this(new HashMap<>());
	}

	private NBTCompound(Map<String, NBTElement<?, ?>> value) {
		super(Type.INSTANCE);
		this.value = value;
	}

	@Override
	public Collection<String> keys() {
		return value.keySet();
	}

	@Override
	public int size() {
		return value.size();
	}

	public NBTElement<?, ?> getElementOrNull(String name) {
		return value.get(name);
	}

	public NBTElement<?, ?> getElement(String name) {
		final NBTElement<?, ?> element = getElementOrNull(name);
		if(element == null)
			throw new NoSuchElementException(name);
		return element;
	}

	public void setElement(String name, NBTElement<?, ?> element) {
		value.put(name, element);
	}

	@Override
	public Object getRawOrNull(String name) {
		NBTElement<?, ?> result = getElementOrNull(name);
		if (result == null)
			return null;

		return result.getRawValue();
	}

	@Override
	public byte getByte(String name) {
		return ((NBTByte) getElement(name)).getValue();
	}

	@Override
	public short getShort(String name) {
		return ((NBTShort) getElement(name)).getValue();
	}

	@Override
	public int getInt(String name) {
		return ((NBTInteger) getElement(name)).getValue();
	}

	@Override
	public long getLong(String name) {
		return ((NBTLong) getElement(name)).getValue();
	}

	@Override
	public float getFloat(String name) {
		return ((NBTFloat) getElement(name)).getValue();
	}

	@Override
	public double getDouble(String name) {
		return ((NBTDouble) getElement(name)).getValue();
	}


	@Override
	public void setByte(String name, byte value) {
		setElement(name, new NBTByte(value));
	}

	@Override
	public void setShort(String name, short value) {
		setElement(name, new NBTShort(value));
	}

	@Override
	public void setInt(String name, int value) {
		setElement(name, new NBTInteger(value));
	}

	@Override
	public void setLong(String name, long value) {
		setElement(name, new NBTLong(value));
	}

	@Override
	public void setFloat(String name, float value) {
		setElement(name, new NBTFloat(value));
	}

	@Override
	public void setDouble(String name, double value) {
		setElement(name, new NBTDouble(value));
	}

	@Override
	public void setRaw(String name, Object value) {
		this.value.put(name, NBTType.fromRawToNBT(value));
	}

	@Override
	public Data getRawValue() {
		return this;
	}

	@Override
	public void writeBinary(File file) {
		try {
			type.writeFile(this, file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static class Type extends NBTType<NBTCompound> {

		public static final Type INSTANCE = new Type();

		public Type() {
			super(10);
		}

		public NBTCompound readFile(File file) throws IOException {
			try(FileInputStream inp = new FileInputStream(file)) {
				return readFull(inp, true);
			}
		}

		public NBTCompound readFull(InputStream in, boolean compressed) throws IOException {
			if(compressed)
				in = new GZIPInputStream(in);

			DataInputStream dataIn = new DataInputStream(in);
			if(dataIn.read() != 10)
				throw new IllegalArgumentException("Invalid input.");

			dataIn.readUTF();

			return read(dataIn);
		}

		public void writeFile(NBTCompound value, File file) throws IOException {
			try(FileOutputStream out = new FileOutputStream(file)) {
				writeFull(value, out, true);
			}
		}

		public void writeFull(NBTCompound value, OutputStream out, boolean compress) throws IOException {
			if(compress)
				out = new GZIPOutputStream(out);
			DataOutputStream dataOut = new DataOutputStream(out);
			dataOut.write(10);
			dataOut.writeUTF("");
			write(value, dataOut);

			if(out instanceof GZIPOutputStream)
				((GZIPOutputStream) out).finish();
		}

		@SuppressWarnings("unchecked")
		@Override
		public void write(NBTCompound value, DataOutputStream out) throws IOException {
			for (Map.Entry<String, NBTElement<?, ?>> entry : value.value.entrySet()) {
				String name = entry.getKey();
				NBTElement element = entry.getValue();

				out.write(element.type.typeID);
				out.writeUTF(name);
				element.type.write(element, out);
			}
			out.write(0);
		}

		@Override
		public NBTCompound read(DataInputStream inp) throws IOException {
			Map<String, NBTElement<?, ?>> result = new HashMap<>();
			while(true) {
				int typeID = inp.read();
				if(typeID == 0)
					break;
				NBTType<?> type = NBTType.getType(typeID);
				String name = inp.readUTF();
				result.put(name, type.read(inp));
			}

			return new NBTCompound(result);
		}
	}

}
