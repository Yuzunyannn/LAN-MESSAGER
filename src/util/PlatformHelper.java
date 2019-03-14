package util;

import java.awt.Toolkit;

import javax.swing.JFrame;

public class PlatformHelper {

	/** 获取操作系统名称 */
	public static String getOSName() {
		return System.getProperty("os.name");
	}

	/** 是否可以最大化 */
	public static boolean canMaximize() {
		return Toolkit.getDefaultToolkit().isFrameStateSupported(JFrame.MAXIMIZED_BOTH);
	}
}
