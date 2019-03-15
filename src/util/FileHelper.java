package util;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;

public class FileHelper {
	/** 从文件里获取图标 */
	public static ImageIcon getIconFromFile(File f) {
		try {
			sun.awt.shell.ShellFolder sf = sun.awt.shell.ShellFolder.getShellFolder(f);
			return new ImageIcon(sf.getIcon(true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}
}
