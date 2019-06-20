package resmgt;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ResourceInfo {

	/** 资源的路径 */
	private final URL url;

	public ResourceInfo(URL url) {
		this.url = url;

	}

	/** Image型 */
	private Image image = null;

	/** 加载资源 */
	public void load() {
		try {
			image = ImageIO.read(url);
		} catch (IOException e) {
		}
	}

	/** 释放资源 */
	public void release() {
		image = null;
	}

	/** 获取对应文件 */
	public File getFile() {
		return new File(url.getFile());
	}

	/** 获取url */
	public URL getURL() {
		return url;
	}

	/** 获取image，如果该文件是img */
	public Image getImage() {
		return image;
	}

	/** 设置加载的image ，让这个资源信息里有image */
	public void setImage(Image img) {
		image = img;
	}

	public ResourceInfo copy() {
		ResourceInfo tmp = new ResourceInfo(this.url);
		tmp.image = this.image;
		return tmp;
	}

}
