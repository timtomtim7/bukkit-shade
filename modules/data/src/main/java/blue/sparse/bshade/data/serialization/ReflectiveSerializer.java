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
		try {
			Class<?> clazz = value.getClass();

			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if((field.getModifiers() & Modifier.STATIC )!= 0)
					continue;

				final Object fieldValue = field.get(value);

			}
		} catch(ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Serializable get(Data source, String name) {
		return null;
	}
}
