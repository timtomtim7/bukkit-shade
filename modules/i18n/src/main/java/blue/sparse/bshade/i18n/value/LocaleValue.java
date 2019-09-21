package blue.sparse.bshade.i18n.value;

import blue.sparse.bshade.i18n.placeholder.parsing.Lexeme;
import blue.sparse.bshade.i18n.placeholder.parsing.Lexer;
import blue.sparse.bshade.i18n.placeholder.parsing.TextIterator;
import blue.sparse.bshade.i18n.placeholder.parsing.token.LiteralStringToken;
import blue.sparse.bshade.i18n.placeholder.parsing.token.RegexTokens;
import blue.sparse.bshade.i18n.placeholder.parsing.token.Token;
import net.md_5.bungee.api.ChatColor;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LocaleValue {

	public static LocaleValue parse(String raw) {
		List<Part> result = new ArrayList<>();

		StringBuilder part = new StringBuilder();

		char[] chars = raw.toCharArray();
		int depth = 0;
		boolean escaped = false;

		for (char c : chars) {
			if (!escaped) {
				if (c == '\\') {
					escaped = true;
				} else if (c == '{') {
					if (depth == 0) {
						result.add(new StringPart(part.toString()));
						part.setLength(0);
					}
					depth++;
				} else if (c == '}') {
					depth--;
					if (depth < 0)
						throw new IllegalArgumentException("Mismatched brackets!");

					if (depth == 0) {
						result.add(parsePart(part.toString()));
						part.setLength(0);
					}
				} else {
					part.append(c);
				}
			} else {
				part.append(c);
				escaped = false;
			}
		}

		if (depth == 0) {
			result.add(new StringPart(part.toString()));
		} else {
			throw new IllegalArgumentException("Mismatched brackets!");
		}

		return new LocaleValue(result);
	}

	private static Part parsePart(String part) {
		System.out.println("-----");
		Set<Token> tokens = RegexTokens.valuesAsSet();
		tokens.add(LiteralStringToken.INSTANCE);

		Lexer lexer;
		try {
			lexer = new Lexer(new TextIterator(part, "part"), tokens);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		List<Lexeme> lexemes = new ArrayList<>();

		while (lexer.hasNext()) {
			Lexer.Result result = lexer.next();
			if (result instanceof Lexer.Result.Success) {
				lexemes.add(((Lexer.Result.Success) result).lexeme);
			} else {
				throw new IllegalArgumentException("Failed to parse " + result);
			}
		}

		return parsePart(lexemes);
	}

	private static Part parsePart(List<Lexeme> lexemes) {
		if(lexemes.size() == 1) {
			Lexeme lexeme = lexemes.get(0);
			if(lexeme.token != RegexTokens.IDENTIFIER)
				return new SimplePlaceholderPart(lexeme.contents);
		}

		return new StringPart("{}");
	}

	public static void main(String[] args) {
		System.out.println(parse("A simple test."));
		System.out.println(parse("A message with {value} placeholders."));
		System.out.println(parse("Buy {count}x items for ${price #,###.00}."));
		System.out.println(parse("Flight: {if enabled \"&aEnabled\" else \"&cDisabled\"}"));
	}

//	public void send(CommandSender target, Map<String, Object> placeholders) {
//		target.sendMessage(toString(placeholders));
//	}

	private List<Part> parts;

	public LocaleValue(List<Part> parts) {
		this.parts = parts;
	}

	public String toString(Object... placeholders) {
		return toString(toMap(placeholders));
	}

	public String toString(Map<String, Object> placeholders) {
		return ChatColor.translateAlternateColorCodes(
				'&',
				parts.stream()
				.map(it -> it.toString(placeholders))
				.collect(Collectors.joining(""))
		);
	}

	@Override
	public String toString() {
		return ChatColor.translateAlternateColorCodes(
				'&',
				parts.stream()
				.map(Part::toString)
				.collect(Collectors.joining(""))
		);
	}

	public String raw() {
		return parts.stream()
				.map(Part::raw)
				.collect(Collectors.joining(""));
	}

	private static Map<String, Object> toMap(Object[] objects) {
		final Map<String, Object> result = new HashMap<>();

		if (objects.length % 2 != 0)
			throw new IllegalArgumentException("Mis-matched value and key count.");

		for (int i = 0; i < objects.length; i += 2) {
			Object keyObj = objects[i];
			if (!(keyObj instanceof String))
				throw new IllegalArgumentException("Keys must be strings");

			result.put((String) keyObj, objects[i + 1]);
		}

		return result;
	}

}
