package blue.sparse.bshade.data.example;

import blue.sparse.bshade.data.Data;

import java.io.File;
import java.util.Arrays;

public class Test {
	public static void main(String[] args) {
		Data.create(it -> {
			it.setInt("five", 5);
			it.setIntArray("someIntArray", 1, 2, 3, 4, 5);

			it.setData("nested", n -> {
				n.setString("hello", "world");
			});

			it.writeBinary(new File("test.dat"));
		});

		Data read = Data.readBinary(new File("test.dat"));
		System.out.println(read.keys());
		System.out.println(Arrays.toString(read.getIntArray("someIntArray")));
	}
}
