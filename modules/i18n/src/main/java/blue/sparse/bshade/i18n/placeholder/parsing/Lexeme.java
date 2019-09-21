package blue.sparse.bshade.i18n.placeholder.parsing;

import blue.sparse.bshade.i18n.placeholder.parsing.token.Token;

import java.util.Objects;

public final class Lexeme {
	public final Token token;
	public final String contents;
	public final SourcePosition positionStart;
	public final SourcePosition positionEnd;

	public Lexeme(
			Token token,
			String contents,
			SourcePosition positionStart,
			SourcePosition positionEnd
	) {
		this.token = token;
		this.contents = contents;
		this.positionStart = positionStart;
		this.positionEnd = positionEnd;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Lexeme)) return false;
		Lexeme lexeme = (Lexeme) o;
		return Objects.equals(token, lexeme.token) &&
				Objects.equals(contents, lexeme.contents) &&
				Objects.equals(positionStart, lexeme.positionStart) &&
				Objects.equals(positionEnd, lexeme.positionEnd);
	}

	@Override
	public int hashCode() {
		return Objects.hash(token, contents, positionStart, positionEnd);
	}

	@Override
	public String toString() {
		return String.format(
				"Lexeme{token=%s, contents='%s'}",
				token,
				contents.replace("\n", "\\n")
//				positionStart,
//				positionEnd
		);
	}
}
