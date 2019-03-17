package core;

import client.frame.MainFrame;
import platform.Platform;

public class Main {

	public static void main(String[] args) {
		System.out.println("当前操作系统：" + Platform.getOSName());
		new MainFrame();
	}

}
