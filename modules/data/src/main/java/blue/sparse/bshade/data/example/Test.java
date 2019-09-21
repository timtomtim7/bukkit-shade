package blue.sparse.bshade.data.example;

import blue.sparse.bshade.data.Data;
import blue.sparse.bshade.data.list.DataList;

import java.io.File;
import java.util.UUID;

public class Test {

	public static void main(String[] args) {
		Data.create(data -> {
			data.setDataList("matches",
					Data.create(b -> {
						b.setDouble("double", 53.0);
						b.setString("string", "hello");
						b.setSerialized("uuid", UUID.randomUUID());
					}),
					Data.create(b -> {
						b.setDouble("double", 25.0);
						b.setString("string", "world");
						b.setSerialized("uuid", UUID.randomUUID());
					})
			);

			data.writeBinary(new File("test.dat"));
		});

		Data data = Data.readBinary(new File("test.dat"));
		DataList list = data.getDataList("matches");
		Data first = list.get(0);
		System.out.println(first);
		System.out.println(first.getSerialized("uuid"));
//		Data.create().writeBinary(new File("test.dat"));
	}
}
