package platform;

import java.awt.Toolkit;

import javax.swing.JFrame;

/** 平台管理类 */
public class Platform {

	public static final Platform platform = new Platform();

	final public OS os;
	final public String name;

	public Platform() {
		this.name = System.getProperty("os.name");
		if (name.toLowerCase().indexOf("windows") != -1)
			this.os = OS.WINDOWS;
		else if (name.toLowerCase().indexOf("mac") != -1)
			this.os = OS.MAC;
		else
			this.os = OS.LINUX;
	}

	/** 获取操作系统名称 */
	public static String getOSName() {
		return platform.name;
	}

	/** 获取操作系统 */
	static public OS getOS() {
		return platform.os;
	}

	/** 是否可以最大化 */
	public static boolean canMaximize() {
		return Toolkit.getDefaultToolkit().isFrameStateSupported(JFrame.MAXIMIZED_BOTH);
	}

}
