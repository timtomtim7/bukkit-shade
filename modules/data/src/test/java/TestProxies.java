import blue.sparse.bshade.data.example.Test;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class TestProxies {



	public static void main(String[] args) {
		Test test = ConfigProxy.get(Test.class);

		test.position().get().setX(5);

		System.out.println("Config value 'a' is " + test.a().get());
		System.out.println("Config value 'd' is " + test.d().get());
	}

	interface Test extends ConfigProxy {
		// Everything that does not extend (implement) `ConfigProxy` should be boxed with `Value`
		Value<Integer> a();
		Value<Boolean> b();

		// Subsections cannot be boxed with `Value`!
		SomeSection someSection();

		List<SomeSection> listOfSections();

		// Non-primitive classes can also be used as values.
		// This will NOT auto-save in the config.
		// thing().get().setX(5);
		Value<Position> position();

		Value<List<Position>> listOfPositions();

		default Value<Integer> d() {
			return new Value<>(42);
		}

		interface SomeSection extends ConfigProxy {
		}
	}

	public class Position {
		private int x;
		private int y;
		private int z;

		public Position(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getZ() {
			return z;
		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}

		public void setZ(int z) {
			this.z = z;
		}
	}

	public static class Value<T> {
		private T value;

		public Value(T value) {
			this.value = value;
		}

		public void set(T value) {
			this.value = value;
		}

		public T get() {
			return value;
		}
	}

	@interface Comment {
		String value();
	}

	@interface Configurable {
		String value();
	}

	interface ConfigProxy {
		@SuppressWarnings("unchecked")
		static <T extends ConfigProxy> T get(Class<T> clazz) {
			return (T) Proxy.newProxyInstance(
					clazz.getClassLoader(),
					new Class[]{clazz},
					new ConfigProxyHandler(clazz)
			);
		}
	}

	private static class ConfigProxyHandler implements InvocationHandler {

		private Class<? extends ConfigProxy> original;

		public ConfigProxyHandler(Class<? extends ConfigProxy> original) {
			this.original = original;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.isDefault()) {
				return MethodHandles.lookup()
						.in(method.getDeclaringClass())
						.unreflectSpecial(method, method.getDeclaringClass())
						.bindTo(proxy)
						.invokeWithArguments();
			}

			System.out.printf(
					"name: \"%s\", type: %s%n",
					method.getName(),
					method.getReturnType().toString()
			);

			return 25;
		}
	}

}
