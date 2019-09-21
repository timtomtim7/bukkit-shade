package blue.sparse.bshade.i18n.placeholder.parsing.token;

public interface Token {

    boolean isFullyValid(CharSequence value);

    boolean isPartiallyValid(CharSequence value);

}
