package blue.sparse.bshade.i18n.placeholder;

import java.util.*;

public class PlaceholderStack implements Placeholders {

	private static final ThreadLocal<PlaceholderStack> global = ThreadLocal.withInitial(PlaceholderStack::new);

	public static PlaceholderStack global() {
		return global.get();
	}

	private final Stack<Frame> stack = new Stack<>();

	public PlaceholderStack() {
	}

	public Frame current() {
		return stack.peek();
	}

	public Frame push() {
		return stack.push(new Frame());
	}

	public Frame push(Placeholders placeholders) {
		Frame frame = push();
		frame.add(placeholders);
		return frame;
	}

	public Frame pushMap(Map<String, Object> map) {
		Frame frame = push();
		addMap(map);
		return frame;
	}

	public Frame pushMap(Object... keysToValues) {
		Frame frame = push();
		addMap(keysToValues);
		return frame;
	}

	public Frame push(String prefix, Object object) {
		Frame frame = push();
		add(prefix, object);
		return frame;
	}

	public void addMap(Map<String, Object> map) {
		current().add(Placeholders.map(map));
	}

	public void addMap(Object... keysToValues) {
		current().add(Placeholders.map(keysToValues));
	}

	public void add(String prefix, Object object) {
		current().add(Placeholders.fields(prefix, object));
	}

	public void pop() {
		if(stack.size() == 1)
			throw new EmptyStackException();

		stack.pop();
	}

	@Override
	public String get(String key) {
		return stack.stream()
				.map(it -> it.get(key))
				.filter(Objects::nonNull)
				.findFirst()
				.orElse(null);
	}

	public class Frame implements Placeholders {
		private final Set<Placeholders> placeholders;

		private Frame() {
			this.placeholders = new LinkedHashSet<>();
		}

		public void add(Placeholders placeholders) {
			if(placeholders instanceof PlaceholderStack)
				throw new IllegalArgumentException("Cannot add a PlaceholderStack to a PlaceholderStack");
			this.placeholders.add(placeholders);
		}

		@Override
		public String get(String key) {
			return placeholders.stream()
					.map(it -> it.get(key))
					.filter(Objects::nonNull)
					.findFirst()
					.orElse(null);
		}
	}

}
