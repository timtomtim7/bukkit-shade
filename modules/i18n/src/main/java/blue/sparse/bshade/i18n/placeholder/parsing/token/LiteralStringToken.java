package blue.sparse.bshade.i18n.placeholder.parsing.token;

public final class LiteralStringToken implements Token {
    public static final LiteralStringToken INSTANCE = new LiteralStringToken();

    private LiteralStringToken() {}

    @Override
    public boolean isFullyValid(CharSequence value) {
        return parse(value.toString()) == Result.VALID;
    }

    @Override
    public boolean isPartiallyValid(CharSequence value) {
        final Result result = parse(value.toString());
        return result == Result.PARTIAL || result == Result.VALID;
    }

    public Result parse(CharSequence value) {
        if(value.length() <= 0)
            return Result.PARTIAL;
        if(value.charAt(0) != '"')
            return Result.INVALID;
        if(value.length() <= 1)
            return Result.PARTIAL;
        if(value.chars().anyMatch(it -> it != 9 && it < 32))
            return Result.INVALID;
        if(value.charAt(value.length() - 1) != '"')
            return Result.PARTIAL;

        boolean escaped = false;
        for(int i = 1; i < value.length() - 1; i++) {
            final char c = value.charAt(i);

            if(escaped) {
                escaped = false;
            } else if(c == '\\') {
                escaped = true;
            } else if(c == '\"') {
                return Result.INVALID;
            }
        }

        return escaped ? Result.PARTIAL : Result.VALID;
    }

    @Override
    public String toString() {
        return "LITERAL_STRING_TOKEN";
    }

    enum Result {
        VALID,
        PARTIAL,
        INVALID
    }
}
