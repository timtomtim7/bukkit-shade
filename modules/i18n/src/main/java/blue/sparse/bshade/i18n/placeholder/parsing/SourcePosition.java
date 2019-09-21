package blue.sparse.bshade.i18n.placeholder.parsing;

import java.util.Objects;

public final class SourcePosition {

	public final String fileName;

	public final int line;
	public final int column;
	public final int index;

	public SourcePosition(String fileName, int line, int column, int index) {
		this.fileName = fileName;
		this.index = index;
		this.column = column;
		this.line = line;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SourcePosition)) return false;
		SourcePosition that = (SourcePosition) o;
		return index == that.index &&
				column == that.column &&
				line == that.line &&
				fileName.equals(that.fileName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fileName, index, column, line);
	}

	@Override
	public String toString() {
		return String.format(
				"SourcePosition{fileName='%s', line=%d, column=%d, index=%d}",
				fileName,
				line,
				column,
				index
		);
	}
}
