package util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassHelper {

	/** 根据当前项目获取运行时url */
	public static URL getRuntimeURL(String packageName) {
		URL url = null;
		if (packageName.isEmpty()) {
			// 先获取一下有的包
			url = ClassLoader.getSystemResource("core");
			if (url != null)
				try {
					url = new URL(url.toString().substring(0, url.toString().length() - 4));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
		} else
			url = ClassLoader.getSystemResource(packageName);
		return url;
	}

	/**
	 * 寻找本文件内的所有类
	 * 
	 */
	public static List<Class<?>> findClasses(URL url, String packageName, final boolean recursive) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		List<Class<?>> clsList = new LinkedList<Class<?>>();
		if (url == null)
			return clsList;
		String protocol = url.getProtocol();
		if (protocol.equals("file")) {
			String filePath;
			try {
				filePath = URLDecoder.decode(url.getFile(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return clsList;
			}
			File rootDir = new File(filePath);
			findClassesWithFile(clsList, packageName, rootDir, recursive, loader);
			return clsList;
		} else if (protocol.equals("jar")) {
			JarFile jar = null;
			try {
				jar = ((JarURLConnection) url.openConnection()).getJarFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				return clsList;
			}
			findClassesWithJar(clsList, packageName, jar, recursive, loader);
			return clsList;
		}
		return clsList;
	}

	/** 寻找一个文件内的所有类 */
	public static void findClassesWithJar(List<Class<?>> clsList, String packageName, JarFile jar,
			final boolean recursive, ClassLoader loader) {
		Enumeration<JarEntry> entries = jar.entries();
		// 遍历所有jar中的元素
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			name = name.replace('/', '.');
			if (name.startsWith(packageName)) {
				// 如果这个是个包
				if (name.charAt(name.length() - 1) == '.') {
					name = name.substring(0, name.length() - 2);
					if (recursive == false && !name.equals(packageName))
						break;
					else
						continue;
				}
				// 如果是class
				if (name.endsWith(".class")) {
					String classPath = name.substring(0, name.length() - 6);
					try {
						Class<?> cls = loader.loadClass(classPath);
						clsList.add(cls);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/** 寻找一个文件内的所有类 */
	public static void findClassesWithFile(List<Class<?>> clsList, String packageName, File rootFile,
			final boolean recursive, ClassLoader loader) {
		if (!rootFile.exists() || !rootFile.isDirectory())
			return;
		// 筛选器
		FileFilter useFFilter = new FileFilter() {
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		};
		File[] files = rootFile.listFiles(useFFilter);
		for (File file : files) {
			// 如果是文件夹
			if (file.isDirectory()) {
				// 递归再次寻找
				String newPackageName = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
				findClassesWithFile(clsList, newPackageName, file, recursive, loader);
				continue;
			}
			String className = file.getName().substring(0, file.getName().length() - 6);
			try {
				String classPath = packageName.isEmpty() ? className : packageName + "." + className;
				Class<?> cls = loader.loadClass(classPath);
				clsList.add(cls);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
