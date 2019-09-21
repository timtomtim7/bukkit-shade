package blue.sparse.bshade.i18n.placeholder.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TextIterator {

    private final BufferedReader text;
    private final String fileName;
    private final List<QueuedCharacter> queue = new ArrayList<>();
    private final List<QueuedCharacter> previous = new ArrayList<>();

    private int readLine = 1;
    private int readColumn = 0;
    private int readIndex = 0;

    public TextIterator(BufferedReader text, String fileName) {
        this.text = text;
        this.fileName = fileName;
    }

    public TextIterator(String content, String fileName) {
        this(new BufferedReader(new StringReader(content)), fileName);
    }

    public boolean hasNext() throws IOException {
        read();
        return !queue.isEmpty();
    }

    public char next() throws IOException {
        read();
        if(!queue.isEmpty()) {
            final QueuedCharacter queued = queue.remove(0);
            previous.add(queued);
//            System.out.println(queue.size() + " " + previous.size());
            return queued.value;
        }

        throw new NoSuchElementException();
    }

    public SourcePosition nextPosition() throws IOException {
        read();
        if(queue.isEmpty())
            throw new NoSuchElementException();
        return queue.get(0).position;
    }

    public SourcePosition position() {
        if(previous.isEmpty())
            return getReadPosition();
        return previous.get(0).position;
    }

    public void reverse(int count) {
        for(int i = 0; i < count; i++) {
            queue.add(0, previous.remove(previous.size() - 1));
        }
    }

    private SourcePosition getReadPosition() {
        return new SourcePosition(fileName, readLine, readColumn, readIndex);
    }

    private void read() throws IOException {
        final int read = text.read();
        if(read == -1)
            return;

        final char c = (char) read;

        readIndex++;
        if (c == '\r') {
            read();
            return;
        }

        readColumn++;
        if (c == '\n') {
            readLine++;
            readColumn = 0;
        }

        queue.add(new QueuedCharacter(c, getReadPosition()));
    }

    private class QueuedCharacter {
        private final char value;
        private final SourcePosition position;

        private QueuedCharacter(char value, SourcePosition position) {
            this.value = value;
            this.position = position;
        }
    }

}
