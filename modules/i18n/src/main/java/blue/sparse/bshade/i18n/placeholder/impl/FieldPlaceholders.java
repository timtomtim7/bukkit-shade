package blue.sparse.bshade.i18n.placeholder.impl;

import blue.sparse.bshade.i18n.placeholder.Placeholders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FieldPlaceholders implements Placeholders {
	private final String prefix;
	private final Object object;
	private final Class<?> clazz;

	public FieldPlaceholders(String prefix, Object object) {
		this.prefix = prefix;
		this.object = object;
		this.clazz = object.getClass();
	}

	@Override
	public String get(String key) {
		String[] split = key.split("\\.");
		if(split.length == 0)
			return null;

		int startIndex = 0;
		if(prefix != null) {
			if(!split[0].equalsIgnoreCase(prefix))
				return null;

			startIndex = 1;
		}

		Object currentValue = object;
		Class<?> currentClass = clazz;

		for (int i = startIndex; i < split.length; i++) {
			String s = split[i];
			if (currentValue == null)
				return null;
			if (s.isEmpty())
				return null;

			try {
				Field field = currentClass.getDeclaredField(s);
				field.setAccessible(true);

				currentValue = field.get(currentValue);
				if (currentValue == null)
					return null;

				currentClass = currentValue.getClass();
				continue;
			} catch (NoSuchFieldException ignored) {

			} catch (Throwable ignored) {
				return null;
			}

			try {
				StringBuilder getterName = new StringBuilder("get");
				getterName.append(Character.toUpperCase(s.charAt(0)));
				if (s.length() > 1)
					getterName.append(s.substring(1));

				Method method = currentClass.getDeclaredMethod(getterName.toString());
				method.setAccessible(true);

				currentValue = method.invoke(currentValue);
				if (currentValue == null)
					return null;

				currentClass = currentValue.getClass();
			} catch (Throwable ignored) {
				return null;
			}
		}

		return currentValue.toString();
	}
}
