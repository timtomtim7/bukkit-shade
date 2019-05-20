package blue.sparse.bshade.versions.util;

import java.util.function.IntFunction;

public class ColoredScroller {
    protected ColoredTextBuffer current = new ColoredTextBuffer("");
    protected IntFunction<String> content;
    protected int contentIndex;
    protected int width;
    protected int offset;

    public ColoredScroller(int width, IntFunction<String> content) {
        this.width = width;
        this.content = content;
    }

    protected void cutExtra(int position) {
        int diff = position - offset;
        offset = position;
        current.trim(diff, current.length());
    }

    protected void ensureContent(int position, int length) {
        if (position % 10 == 0) {
            cutExtra(position);
        }

        int relativePosition = position - offset;
        while (current.length() - relativePosition < length) {
            current.append(this.content.apply(contentIndex++));
        }
    }

    public String get(int frame) {
        ensureContent(frame, width);
        frame -= offset;
//        String content = current.toString();
        ColoredTextBuffer content = current.clone();

        ColoredTextBuffer result = new ColoredTextBuffer("");

        int endEndIndex = ((frame + width - 1) % content.length()) + 1;
        int endStartIndex = Math.max(0, endEndIndex - width);

        int endWidth = endEndIndex - endStartIndex;
        if (endWidth < width) {
            int extra = width - endWidth;
            int innerCount = extra / content.length();
            int startIndex = content.length() - (extra % content.length());

            CharSequence start = content.subSequence(startIndex, content.length());
            result.append(start);
            for (int i = 0; i < innerCount; i++) {
                result.append(content);
            }
        }

        CharSequence ending = content.subSequence(endStartIndex, endEndIndex);
        result.append(ending);

        return result.toString();

    }

    public static void main(String[] args) {
        final String str = "&aThis &bis &ccolorful &land bold &dor maybe pink.".replace('&', '\u00a7');
        final ColoredScroller scroller = new ColoredScroller(20, i -> str);

        for (int i = 0; i < 1000; i++) {
            System.out.println(scroller.get(i));
        }
    }
}
