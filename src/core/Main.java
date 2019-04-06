package core;

import client.frame.MainFrame;
import platform.Platform;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// RecvDealMessage.registerMessage(1, DebugMessageString.class);
		System.out.println("当前平台：" + Platform.platform);
		new MainFrame();
		// try {
		// Network net = new Network(25565);
		// while (true)
		// Thread.sleep(2000);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

}
