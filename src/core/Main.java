package core;

import client.frame.MainFrame;
import util.PlatformHelper;

public class Main {

	public static void main(String[] args) {
		System.out.println("当前操作系统：" + PlatformHelper.getOSName());
		new MainFrame();
	}

}
