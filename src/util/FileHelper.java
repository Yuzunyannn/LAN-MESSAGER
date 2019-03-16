package util;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class FileHelper {
	/** 从文件里获取图标 */
	public static ImageIcon getIconFromFile(File f) {
		try {
			sun.awt.shell.ShellFolder sf = sun.awt.shell.ShellFolder.getShellFolder(f);	
			return new ImageIcon(sf.getIcon(true));
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**从macOS平台获取文件图标*/
	public static Icon getIconFromMac(File f) {
		try {
			final javax.swing.JFileChooser fc = new javax.swing.JFileChooser(); 
			return fc.getUI().getFileView(fc).getIcon(f);
		} catch (Exception e) {
			e.printStackTrace();		
		}
		return null;
	}
}
