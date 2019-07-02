package client.frame;

import java.io.IOException;

public class Restart {
	public static void restartApplication() {
		try {
			Runtime.getRuntime().exec("java -jar " + System.getProperty("java.class.path"));
			System.exit(0);
			System.out.println();
		} catch (IOException e) {
		}
	}
}
