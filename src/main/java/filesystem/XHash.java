package filesystem;

import java.io.FileInputStream;
import java.io.IOException;

import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.XXHashFactory;

public class XHash {

	public static int Hash(String path) {
		FileInputStream file = null;
		XXHashFactory factory = XXHashFactory.safeInstance();
		int seed = 0x9747ffff;

		StreamingXXHash32 hash32 = factory.newStreamingHash32(seed);
		
		byte[] buffer = new byte[1024 * 32];
		int read = -1;

		try {
			file = new FileInputStream(path);
			while ((read = file.read(buffer)) != -1) {
				hash32.update(buffer, 0, read);
			}
			file.close();

		} catch (IOException e) {

		}
		
		return hash32.getValue();
	}
}
