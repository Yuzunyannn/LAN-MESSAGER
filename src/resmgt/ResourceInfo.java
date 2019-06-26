package resmgt;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import log.Logger;
import nbt.NBTStream;
import nbt.NBTTagCompound;

public class ResourceInfo {

	/** 资源的路径 */
	private final URL url;

	/** 资源类型 */
	ResourceInfo.Type type = ResourceInfo.Type.NUKNOW;

	public ResourceInfo(URL url) {
		this.url = url;
	}

	/** 加载的数据 */
	private Object obj = null;

	/** 加载资源 */
	public void load() {
		try {
			obj = ImageIO.read(url);
		} catch (IOException e) {
		}
		if (obj != null) {
			this.type = ResourceInfo.Type.IMAGE;
			return;
		}
		try {
			obj = NBTStream.read(this.getFile());
		} catch (IOException e) {
		}
		if (obj != null) {
			this.type = ResourceInfo.Type.NBT;
			return;
		}
	}

	/** 释放资源 */
	public void release() {
		obj = null;
		type = ResourceInfo.Type.NUKNOW;
	}

	/** 保存 */
	public void save() {
		if (this.getType() == ResourceInfo.Type.NBT) {
			File file = this.getFile();
			try {
				NBTStream.write(file, this.getNBT());
			} catch (IOException e) {
				Logger.log.warn("nbt数据保存失败！", e);
			}
		} else {
			Logger.log.warn("不支持的保存");
		}
	}

	/** 获取资源类型 */
	public ResourceInfo.Type getType() {
		return type;
	}

	/** 获取对应文件 */
	public File getFile() {
		return new File(url.getPath().replace("%20", " "));
	}

	/** 获取url */
	public URL getURL() {
		return url;
	}

	/** 获取image，如果该文件是IMAGE */
	public Image getImage() {
		if (this.getType() == ResourceInfo.Type.IMAGE)
			return (Image) obj;
		return null;
	}

	/** 获取nbt，如果该文件是NBT */
	public NBTTagCompound getNBT() {
		if (this.getType() == ResourceInfo.Type.NBT)
			return (NBTTagCompound) obj;
		return null;
	}

	/** 设置加载的资源 */
	public void setData(Object obj) {
		this.obj = obj;
		if (this.obj instanceof Image)
			this.type = ResourceInfo.Type.IMAGE;
		else if (this.obj instanceof NBTTagCompound)
			this.type = ResourceInfo.Type.NBT;
	}

	public ResourceInfo copy() {
		ResourceInfo tmp = new ResourceInfo(this.url);
		tmp.obj = this.obj;
		tmp.type = this.type;
		return tmp;
	}

	public static enum Type {
		NUKNOW, IMAGE, NBT
	}
}
