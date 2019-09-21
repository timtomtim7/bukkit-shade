package blue.sparse.bshade.menu.element;

import blue.sparse.bshade.menu.MenuPosition;
import org.apache.commons.lang.Validate;

import java.util.*;

public interface ElementShape extends Iterable<MenuPosition> {

	boolean contains(MenuPosition position);

	default boolean overlaps(ElementShape other) {
		for(MenuPosition pos : this) {
			if(other.contains(pos))
				return true;
		}

		return false;
	}

	static Map<Character, ElementShape> createAllFromLayout(String[] layout) {
		if(layout.length == 0)
			throw new IllegalArgumentException("Invalid layout");

		Map<Character, List<MenuPosition>> positions = new HashMap<>();

		int length = layout[0].length();
		for (int y = 0; y < layout.length; y++) {
			String line = layout[y];
			if(line.length() != length)
				throw new IllegalArgumentException("Invalid layout");

			char[] chars = line.toCharArray();
			for (int x = 0; x < chars.length; x++) {
				char c = chars[x];

				positions.computeIfAbsent(c, m -> new ArrayList<>())
						.add(new MenuPosition(x, y));
			}
		}

		Map<Character, ElementShape> result = new HashMap<>();
		for (Map.Entry<Character, List<MenuPosition>> entry : positions.entrySet()) {
			List<MenuPosition> list = entry.getValue();
			if(list.size() == 1) {
				result.put(entry.getKey(), new Single(list.get(0)));
			}else {
				result.put(entry.getKey(), new Set(list));
			}
		}

		return result;
	}

	class Immutable implements ElementShape {

		private final ElementShape original;

		public Immutable(ElementShape original) {
			this.original = original;
		}

		@Override
		public boolean contains(MenuPosition position) {
			return original.contains(position);
		}

		@Override
		public Iterator<MenuPosition> iterator() {
			return original.iterator();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Immutable)) {
				if(o instanceof ElementShape)
					return o.equals(original);
				return false;
			}
			Immutable that = (Immutable) o;
			return original.equals(that.original);
		}

		@Override
		public int hashCode() {
			return Objects.hash(original);
		}
	}

	class Single implements ElementShape {
		public final MenuPosition position;

		public Single(MenuPosition position) {
			Validate.notNull(position);
			this.position = position;
		}

		@Override
		public boolean contains(MenuPosition position) {
			return Objects.equals(this.position, position);
		}

		@Override
		public Iterator<MenuPosition> iterator() {
			return Collections.singleton(position).iterator();
		}
	}

	class Rectangle implements ElementShape {

		public final MenuPosition min;
		public final MenuPosition max;

		private final List<MenuPosition> all;

		public Rectangle(MenuPosition a, MenuPosition b) {
			this.min = new MenuPosition(Math.min(a.x, b.x), Math.min(a.y, b.y));
			this.max = new MenuPosition(Math.max(a.x, b.x), Math.max(a.y, b.y));
			this.all = new ArrayList<>();

			for(int x = min.x; x < max.x; x++) {
				for(int y = min.y; y < max.y; y++) {
					this.all.add(new MenuPosition(x, y));
				}
			}
		}

		@Override
		public boolean contains(MenuPosition position) {
			return position.x >= min.x
					&& position.y >= min.y
					&& position.x < max.x
					&& position.y < max.y;
		}

		@Override
		public Iterator<MenuPosition> iterator() {
			return this.all.iterator();
		}
	}

	class Set implements ElementShape {

		private final java.util.Set<MenuPosition> positions;

		public Set(Collection<MenuPosition> positions) {
			this.positions = new HashSet<>(positions);
		}


		@Override
		public boolean contains(MenuPosition position) {
			return positions.contains(position);
		}

		@Override
		public Iterator<MenuPosition> iterator() {
			return positions.iterator();
		}
	}

}
