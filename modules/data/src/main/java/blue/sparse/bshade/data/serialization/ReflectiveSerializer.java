package blue.sparse.bshade.data.serialization;

import blue.sparse.bshade.data.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectiveSerializer implements Serializer<Object> {

	@Override
	public boolean canSerialize(Object object) {
		return true;
	}

	@Override
	public void set(Data target, String name, Object value) {
		Data result = Data.create();
		if(value == null)
			return;

		try {
			Class<?> clazz = value.getClass();
			result.setString("%", clazz.getName());

			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if((field.getModifiers() & Modifier.STATIC) != 0)
					continue;
				if((field.getModifiers() & Modifier.TRANSIENT) != 0)
					continue;
				field.setAccessible(true);

				final Object fieldValue = field.get(value);
				result.setSerialized(field.getName(), fieldValue);
			}
		} catch(ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}

		target.setData(name, result);
	}

	@Override
	public Serializable get(Data source, String name) {
		return null;
	}

	@Override
	public int getPriority() {
		return -1000;
	}
}
