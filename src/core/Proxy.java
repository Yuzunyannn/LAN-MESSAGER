package core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import log.Logger;
import log.NeedLog;
import platform.Platform;
import util.ClassHelper;

/** 代理类，区分客户端和服务器 */
public class Proxy {

	public final network.Side side;

	public Proxy(network.Side side) {
		this.side = side;
	}

	/** 初始化 */
	public void init() {
		Logger.log.impart("当前平台：" + Platform.platform);
		try {
			// 初始化所有类中带有指定的标签内容
			List<Class<?>> initList = ClassHelper.findClasses(ClassHelper.getRuntimeURL(""), "", true);
			// 扫描所有类
			for (Class<?> cls : initList) {
				Field[] fields = cls.getDeclaredFields();
				for (Field field : fields) {
					if (!Modifier.isStatic(field.getModifiers()))
						continue;
					// 为所有需要log的类赋值
					if (field.getAnnotation(NeedLog.class) != null) {
						field.set(null, Logger.log);
					}
				}
			}
		} catch (Exception e) {
			Logger.log.error("反射类，初始化时出现问题！", e);
			Core.shutdownWithError();
		}
	}

	/** 启动 */
	public void launch() {
	}
}
