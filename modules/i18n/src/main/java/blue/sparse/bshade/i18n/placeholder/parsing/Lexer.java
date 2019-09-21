package blue.sparse.bshade.i18n.placeholder.parsing;

import blue.sparse.bshade.i18n.placeholder.parsing.token.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public final class Lexer implements Iterator<Lexer.Result> {

    private final TextIterator text;
    private final Set<Token> tokens;

    private Lexeme next;
    private Result.Error error;

    public Lexer(
            TextIterator text,
            Set<Token> tokens
    ) throws IOException {
        this.text = text;
        this.tokens = tokens;
        this.next = generateNext();
    }

    public Lexer(
            File file,
            Set<Token> tokens
    ) throws IOException {
        this(new TextIterator(new BufferedReader(new FileReader(file)), file.getName()), tokens);
    }

    public boolean hasNext() {
        return next != null || error != null;
    }

    public Result next() {
        if(error != null) {
            final Result.Error error = this.error;
            this.error = null;
            return error;
        }

        Lexeme result = this.next;
        if (result == null)
            throw new NoSuchElementException();

        try {
            this.next = generateNext();
        } catch (IOException e) {
            this.next = null;
            return new Result.IOError(e);
        }

        return new Result.Success(result);
    }

    private Lexeme generateNext() throws IOException {
        if(!text.hasNext())
            return null;

        List<Token> stillValid = new ArrayList<>(tokens);
        SourcePosition startPosition = text.nextPosition();
        StringBuilder currentText = new StringBuilder();

        List<Lexeme> results = new ArrayList<>();

        while (!stillValid.isEmpty() && text.hasNext()) {
            SourcePosition position = text.nextPosition();

            stillValid.removeIf(it -> !it.isPartiallyValid(currentText));
            stillValid.stream()
                    .filter(it -> it.isFullyValid(currentText))
                    .map(it -> new Lexeme(it, currentText.toString(), startPosition, position))
                    .forEach(results::add);

            currentText.append(text.next());
        }

        if(!stillValid.isEmpty()) {
            SourcePosition position = text.position();
            stillValid.stream()
                    .filter(it -> it.isFullyValid(currentText))
                    .map(it -> new Lexeme(it, currentText.toString(), startPosition, position))
                    .forEach(results::add);
        }

        if (!results.isEmpty()) {
            final Lexeme lexeme = results.get(results.size() - 1);
            text.reverse(currentText.length() - lexeme.contents.length());
            return lexeme;
        } else {
            if(!text.hasNext())
                return null;
            error = new Result.Error(startPosition, "Failed to find matching token.");
            return null;
        }
    }

    public interface Result {
        final class Success implements Result {
            public final Lexeme lexeme;

            public Success(Lexeme lexeme) {
                this.lexeme = lexeme;
            }

            @Override
            public String toString() {
                return String.format("Success{lexer=%s}", lexeme);
            }
        }

        final class IOError implements Result {
            public final IOException exception;

            public IOError(IOException exception) {
                this.exception = exception;
            }

            @Override
            public String toString() {
                return String.format("IOError{exception=%s}", exception);
            }
        }

        final class Error implements Result {
            public final SourcePosition position;
            public final String reason;

            public Error(SourcePosition position, String reason) {
                this.position = position;
                this.reason = reason;
            }

            @Override
            public String toString() {
                return String.format("Error{position=%s, reason='%s'}", position, reason);
            }
        }

    }
}
