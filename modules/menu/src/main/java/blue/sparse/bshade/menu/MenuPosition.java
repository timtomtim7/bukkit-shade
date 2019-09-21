package blue.sparse.bshade.menu;

import java.util.Objects;

public class MenuPosition {

	public final int x;
	public final int y;

	public MenuPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MenuPosition)) return false;
		MenuPosition that = (MenuPosition) o;
		return x == that.x &&
				y == that.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "MenuPosition{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
