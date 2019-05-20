package blue.sparse.bshade.versions.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ColoredTextBuffer implements CharSequence {

	private static final Pattern COLOR_PATTERN =
			Pattern.compile("\u00a7.");

	private static char[] sequenceToCharArray(CharSequence seq) {
		if (seq instanceof ColoredTextBuffer) {
			return ((ColoredTextBuffer) seq).data.clone();
		} else {
			char[] result = new char[seq.length()];

			for (int i = 0; i < result.length; i++) {
				result[i] = seq.charAt(i);
			}

			return result;
		}
	}

	private static List<ChatColor> getColors(CharSequence input) {
		List<ChatColor> result = new ArrayList<>();

		final Matcher matcher = COLOR_PATTERN.matcher(input);
		while (matcher.find()) {
			char colorChar = input.charAt(matcher.start() + 1);
			ChatColor color = ChatColor.getByChar(colorChar);
			result.add(color);
		}
		fixColors(result);

		return result;
	}

	private static void fixColors(List<ChatColor> colors) {
		for (int i = 0; i < colors.size(); i++) {
			ChatColor color = colors.get(i);
			if (color.isColor()) {
				colors.subList(0, i).clear();
			}
		}
	}

	private static String joinColors(List<ChatColor> colors) {
		return colors.stream()
				.map(ChatColor::toString)
				.collect(Collectors.joining(""));
	}

	public static void main(String[] args) {
		final ColoredTextBuffer buffer =
				new ColoredTextBuffer("\u00a7cHello, \u00a7bworld!");
		System.out.println(buffer);

		buffer.trim(2, buffer.length);
		System.out.println(buffer);

		buffer.trim(6, buffer.length - 1);
		System.out.println(buffer);

		buffer.prepend("Hello, w");
		System.out.println(buffer);
	}

	private char[] data;
	private int realLength;
	private int length = -1;
	private int colorCount = 0;

	public ColoredTextBuffer(CharSequence original) {
		this.data = sequenceToCharArray(original);
		this.realLength = data.length;

		recalculate();
	}

	private void recalculate() {
		colorCount = 0;
		for (int i = 0; i < realLength; i++) {
			char c = data[i];
			if (c == '\u00a7')
				colorCount++;
		}

		length = realLength - colorCount * 2;
	}

	public void append(CharSequence seq) {
		char[] appendData = sequenceToCharArray(seq);
		char[] newData = new char[appendData.length + realLength];

		System.arraycopy(data, 0, newData, 0, realLength);
		System.arraycopy(appendData, 0, newData, realLength, appendData.length);

		this.realLength = newData.length;
		this.data = newData;
		recalculate();
	}

	public void prepend(CharSequence seq) {
		char[] prependData = sequenceToCharArray(seq);
		char[] newData = new char[prependData.length + realLength];

		System.arraycopy(prependData, 0, newData, 0, prependData.length);
		System.arraycopy(data, 0, newData, prependData.length, realLength);

		this.realLength = newData.length;
		this.data = newData;
		recalculate();
	}

	public void trim(int startIndex, int endIndex) {
		int realStart = getRealIndex(startIndex);
		int realEnd = getRealIndex(endIndex);

		CharSequence skippedStart = toString().substring(0, realStart);
		char[] colors = joinColors(getColors(skippedStart)).toCharArray();
		int extractedLength = realEnd - realStart;
		char[] newData = new char[extractedLength + colors.length];

		System.arraycopy(colors, 0, newData, 0, colors.length);
		System.arraycopy(data, realStart, newData, colors.length, extractedLength);

		this.realLength = newData.length;
		this.data = newData;
		recalculate();
	}

	@Override
	public int length() {
		if (length == -1)
			recalculate();
		return length;
	}

	@Override
	public char charAt(int index) {
		if (index < 0 || index > length)
			throw new IndexOutOfBoundsException();

		for (int i = 0; i < data.length; i++) {
			char c = data[i];
			if (c == '\u00a7')
				index += 2;
			else if (index == i)
				return c;
		}

		throw new IndexOutOfBoundsException("unexpected");
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		final ColoredTextBuffer result = clone();
		result.trim(start, end);
		return result;
	}

	public ColoredTextBuffer clone() {
		return new ColoredTextBuffer(this);
	}

	private int getRealIndex(int index) {
		if (index < 0 || index > length)
			throw new IndexOutOfBoundsException(String.valueOf(index));

		if(data.length == 0 && index == 0)
			return 0;

		for (int i = 0; i < data.length; i++) {
			char c = data[i];
			if (c == '\u00a7')
				index += 2;
			else if (index == i)
				return index;
		}

		return index;
	}

	@Override
	public String toString() {
		return new String(data);
	}
}
