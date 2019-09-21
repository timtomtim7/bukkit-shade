package blue.sparse.bshade.i18n.placeholder.parsing.token;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum RegexTokens implements Token {
    WHITESPACE("\\h"),
    SYMBOL_PLUS("\\+"),
    SYMBOL_DASH("-"),
    SYMBOL_FORWARD_SLASH("/"),
    SYMBOL_ASTERISK("\\*"),
    SYMBOL_GREATER_THAN(">"),
    SYMBOL_LESS_THAN("<"),
    SYMBOL_EQUALS("="),
    SYMBOL_QUESTION("\\?"),
    SYMBOL_DOT("\\."),
    SYMBOL_COMMA(","),
    SYMBOL_COLON(":"),
    SYMBOL_PAREN_OPEN("\\("),
    SYMBOL_PAREN_CLOSE("\\)"),
    SYMBOL_SCOPE_OPEN("\\{"),
    SYMBOL_SCOPE_CLOSE("\\}"),
    IDENTIFIER("[a-zA-Z_]+[a-zA-Z0-9_]*"),
    KEYWORD_NULL("null"),
    KEYWORD_IF("if"),
    LITERAL_NUMBER_FORMAT("[#0]*[#0,.]*[#0]+"),
    LITERAL_INT("\\d+[ilsb]?"),
    LITERAL_FLOAT("\\d+([fd]|\\.\\d[fd]?)"),
    LITERAL_BOOLEAN("true|false");

    private Pattern pattern;

    RegexTokens(String regex) {
        this(Pattern.compile(regex));
    }

    RegexTokens(Pattern pattern) {
        this.pattern = pattern;
    }

    public static Set<Token> valuesAsSet() {
        return new LinkedHashSet<>(Arrays.asList(values()));
    }

    @Override
    public boolean isFullyValid(CharSequence value) {
        return this.pattern.matcher(value).matches();
    }

    @Override
    public boolean isPartiallyValid(CharSequence value) {
        final Matcher matcher = this.pattern.matcher(value);
        return matcher.matches() || matcher.hitEnd();
    }
}
