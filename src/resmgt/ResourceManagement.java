package resmgt;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import core.Core;
import log.Logger;

public class ResourceManagement {

	/** 实例化对象 */
	public static final ResourceManagement instance = new ResourceManagement();

	/** 提示 */
	public static void impart(String str) {
		Logger.log.impart(str);
	}

	/** 寻找失败 */
	public static void fail(String str, Exception e) {
		Logger.log.error(str, e);
		Core.shutdownWithError();
	}

	/** 警告 */
	public static void warn(String str) {
		Logger.log.warn(str);
	}

	/** 警告 */
	public static void warn(String str, Exception e) {
		Logger.log.warn(str, e);
	}

	/** 记录包内资源路径的map */
	private final Map<String, ResourceInfo> resPack = new HashMap<String, ResourceInfo>();
	/** 记录资源临时路径的map */
	private final Map<String, ResourceInfo> resTmp = new HashMap<String, ResourceInfo>();
	/** 记录数据资源路径的map */
	private final Map<String, ResourceInfo> resData = new HashMap<String, ResourceInfo>();
	/** 当前运行程序的url */
	URL url = ClassLoader.getSystemResource("resources");

	public ResourceManagement() {
		String protocol = url.getProtocol();
		if (protocol.equals("file")) {
			String filePath;
			try {
				filePath = URLDecoder.decode(url.getFile(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				ResourceManagement.fail("资源加载失败！", e);
				return;
			}
			this.findInFile(new File(filePath));
		} else if (protocol.equals("jar")) {
			JarFile jar;
			try {
				jar = ((JarURLConnection) url.openConnection()).getJarFile();
			} catch (IOException e) {
				ResourceManagement.fail("资源加载失败！", e);
				return;
			}
			this.findInJar(jar);
		}
	}

	private void findInFile(File rootFile) {
		if (!rootFile.exists() || !rootFile.isDirectory()) {
			ResourceManagement.warn("未找到任何资源！");
			return;
		}
		// 寻找所有子目录
		File[] files = rootFile.listFiles();
		for (File file : files) {
			// 文件的话，递归查找
			if (file.isDirectory()) {
				this.findInFile(file);
				continue;
			}
			String path = file.getPath();
			path = path.substring(path.indexOf("resources") + "resources".length() + 1);
			path = path.replace('\\', '/');
			try {
				resPack.put(path, new ResourceInfo(file.toURI().toURL()));
			} catch (MalformedURLException e) {
				ResourceManagement.warn("资源转化url时候出现异常！", e);
			}
		}
	}

	private void findInJar(JarFile jar) {
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			if (name.startsWith("resources")) {
				// 如果是包
				if (name.endsWith("/"))
					continue;
				// 如果不是包
				String path = name;
				path = path.substring(path.indexOf("resources") + "resources".length() + 1);
				path = path.replace('\\', '/');
				resPack.put(path, new ResourceInfo(this.getClass().getResource('/' + name)));
			}
		}
	}

	/** 初始化 */
	public void init() {
		for (Entry<String, ResourceInfo> entry : resPack.entrySet()) {
			entry.getValue().load();
			ResourceManagement.impart("资源加载成功：" + entry.getKey());
		}
	}

	/** 获取资源信息，路径从resources包下开始，如img/1.png */
	public ResourceInfo getPackResource(String path) {
		path = path.replace('\\', '/');
		if (resPack.containsKey(path))
			return resPack.get(path);
		return null;
	}

	/** 获取资源信息，临时文件的路径，动态生成的 */
	public ResourceInfo getTmpResource(String path) {
		path = path.replace('\\', '/');
		if (resTmp.containsKey(path))
			return resTmp.get(path);
		return null;
	}

	/** 获取资源信息，data文件夹内数据 */
	public ResourceInfo getDataResource(String path) {
		path = path.replace('\\', '/');
		if (resData.containsKey(path))
			return resData.get(path);
		return null;
	}

	/** 复制已有资源到tmp */
	public ResourceInfo loadTmpResource(ResourceInfo resourceInfo, String virtualPath) {
		if (resTmp.containsKey(virtualPath))
			return resTmp.get(virtualPath);
		resourceInfo = resourceInfo.copy();
		resTmp.put(virtualPath, resourceInfo);
		return resourceInfo;
	}

	/** 加载临时资源，加载完后回调 ，注意线程同步 */
	public void loadTmpResource(String realPath, String virtualPath, IWaitLoad load) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				load.loadFinish(ResourceManagement.this.loadTmpResource(realPath, virtualPath));
			}
		});
	}

	/** 加载临时资源 */
	public ResourceInfo loadTmpResource(String realPath, String virtualPath) {
		virtualPath = virtualPath.replace('\\', '/');
		realPath = realPath.replace('\\', '/');
		if (resTmp.containsKey(virtualPath))
			return resTmp.get(virtualPath);
		File file = new File("tmp/" + realPath);
		if (!file.exists())
			return null;
		ResourceInfo info;
		try {
			info = new ResourceInfo(file.toURI().toURL());
			info.load();
			resTmp.put(virtualPath, info);
		} catch (MalformedURLException e) {
			ResourceManagement.warn("临时资源转化url时候出现异常！", e);
			return null;
		}
		return info;
	}

	/** 加载数据资源 */
	public ResourceInfo loadDataResource(String path) {
		path = path.replace('\\', '/');
		if (resTmp.containsKey(path))
			return resTmp.get(path);
		File file = new File("./data/" + path);
		if (!file.exists())
			return null;
		ResourceInfo info;
		try {
			info = new ResourceInfo(file.toURI().toURL());
			info.load();
			resTmp.put(path, info);
		} catch (MalformedURLException e) {
			ResourceManagement.warn("数据资源转化url时候出现异常！", e);
			return null;
		}
		return info;
	}

	/** 创建一个数据文件 */
	public ResourceInfo loadOorCreateDataResource(String path) {
		path = path.replace('\\', '/');
		if (resTmp.containsKey(path))
			return resTmp.get(path);
		File file = new File("./data/" + path);
		if (!file.exists()) {
			int x = path.lastIndexOf('/');
			if (x > 0) {
				File folder = new File("./data/" + path.substring(0, x));
				folder.mkdirs();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				ResourceManagement.warn("创建数据文件失败！", e);
				return null;
			}
		}
		ResourceInfo info;
		try {
			info = new ResourceInfo(file.toURI().toURL());
			info.load();
			resTmp.put(path, info);
		} catch (MalformedURLException e) {
			ResourceManagement.warn("数据资源转化url时候出现异常！", e);
			return null;
		}
		return info;
	}
}
