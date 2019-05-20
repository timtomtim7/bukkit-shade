import java.io.PrintStream;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;

public class Scrolling {
	public static void main(String[] args) throws InterruptedException {
		String[] test = {
				"one", "two", "three",
				"four", "five", "six",
				"seven", "eight", "nine",
				"ten", "eleven", "twelve",
				"thirteen", "fourteen", "fifteen",
				"sixteen", "seventeen", "eighteen",
				"nineteen", "twenty"
		};

		Scroller scroller = new Scroller(20, i -> test[ThreadLocalRandom.current().nextInt(test.length)] + " ");

//		for (int i = 0; i < 1000; i++) {
//			System.out.println(scroller.get(i));
//		}

		for(int i = 0; true; i++) {
			final PrintStream out = System.out;
//			scroller.update();
			out.print("\r\""+scroller.get(i)+"\"");
			out.flush();
			Thread.sleep(250);
		}
	}

	public static class Scroller {

		private StringBuilder current = new StringBuilder();
		private IntFunction<String> content;
		private int contentIndex;
		private int width;
		private int offset;

		public Scroller(int width, IntFunction<String> content) {
			this.width = width;
			this.content = content;
		}

		private void ensureContent(int position, int length) {
			if (position % 10 == 0) {
				int diff = position - offset;
				offset = position;
				current.delete(0, diff);
			}

			int relativePosition = position - offset;
			while (current.length() - relativePosition < length) {
				current.append(this.content.apply(contentIndex++));
			}
		}

		public String get(int frame) {
			ensureContent(frame, width);
			frame -= offset;
			String content = current.toString();

			StringBuilder result = new StringBuilder();

			int endEndIndex = ((frame + width - 1) % content.length()) + 1;
			int endStartIndex = Math.max(0, endEndIndex - width);

			int endWidth = endEndIndex - endStartIndex;
			if (endWidth < width) {
				int extra = width - endWidth;
				int innerCount = extra / content.length();
				int startIndex = content.length() - (extra % content.length());

				String start = content.substring(startIndex);
				result.append(start);
				for (int i = 0; i < innerCount; i++) {
					result.append(content);
				}
			}

			String ending = content.substring(endStartIndex, endEndIndex);
			result.append(ending);

			return result.toString();

		}
	}
}
