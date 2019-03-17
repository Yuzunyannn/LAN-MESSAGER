package util;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import platform.OS;
import platform.Platform;

public class FileHelper {
	/** 从文件里获取图标 */
	public static Icon getIconFromFile(File f) {
		try {
			if (Platform.getOS() == OS.WINDOWS) {
				sun.awt.shell.ShellFolder sf = sun.awt.shell.ShellFolder.getShellFolder(f);
				return new ImageIcon(sf.getIcon(true));
			} else {
				javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
				return fc.getUI().getFileView(fc).getIcon(f);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
