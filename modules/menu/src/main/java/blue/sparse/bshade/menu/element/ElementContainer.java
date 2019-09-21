package blue.sparse.bshade.menu.element;

import blue.sparse.bshade.menu.Layout;
import java.util.*;

public class ElementContainer extends AbstractSet<Element> {

	private final Set<Element> elements = new HashSet<>();
	private Layout layout;

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public void setLayout(String... layout) {
		setLayout(new Layout(layout));
	}

	public int getHeight() {
		return layout.getHeight();
	}

	public int getWidth() {
		return layout.getWidth();
	}

	public boolean canAdd(Element element) {
		for (Element other : elements) {
			if (other.shape.overlaps(element.shape)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean add(Element element) {
		if(!canAdd(element))
			return false;

		return elements.add(element);
	}

	@Override
	public boolean remove(Object o) {
		return elements.remove(o);
	}

	@Override
	public Iterator<Element> iterator() {
		return elements.iterator();
	}

	@Override
	public int size() {
		return elements.size();
	}

}
