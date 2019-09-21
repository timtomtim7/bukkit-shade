package blue.sparse.bshade.menu;

import blue.sparse.bshade.menu.element.ElementShape;

import java.util.*;

public final class Layout {

	private final String[] layout;
	private final Map<Character, ElementShape> shapes;

	public Layout(String[] layout) {
		if(layout == null)
			throw new IllegalArgumentException("Layout cannot be null.");

		if(layout.length == 0 || layout.length > 7)
			throw new IllegalArgumentException("Invalid layout height.");

		int width = layout[0].length();
		if(Arrays.stream(layout).anyMatch(it -> it.length() != width))
			throw new IllegalArgumentException("Inconsistent layout width.");

		this.layout = Arrays.copyOf(layout, layout.length);
		this.shapes = ElementShape.createAllFromLayout(layout);
	}

	public int getHeight() {
		return layout.length;
	}

	public int getWidth() {
		return layout[0].length();
	}

	public Set<Character> getCharacters() {
		return Collections.unmodifiableSet(shapes.keySet());
	}

	public Collection<ElementShape> getShapes() {
		return Collections.unmodifiableCollection(shapes.values());
	}

	public ElementShape getShape(char character) {
		return new ElementShape.Immutable(shapes.get(character));
	}

	public ElementShape getShapeAt(int x, int y) {
		return getShape(getCharacterAt(x, y));
	}

	public Character getCharacterAt(int x, int y) {
		return layout[y].charAt(x);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Layout)) return false;
		Layout layout1 = (Layout) o;
		return Arrays.equals(layout, layout1.layout);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(layout);
	}
}
