package core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import log.Logger;
import log.NeedLog;
import network.RecvDealMessage;
import platform.Platform;
import story.Story;
import story.message.MSBegin;
import story.message.MSEnd;
import story.message.MSMemberIn;
import story.message.MSNbtSend;
import transfer.StoryFileSender;
import transfer.message.UMFileSendToUser;
import user.message.MUGULRequest;
import user.message.MUSSearch;
import user.message.MUSString;
import user.message.MessageLogin;
import util.ClassHelper;

/** 代理类，区分客户端和服务器 */
public class Proxy {

	public final network.Side side;

	public Proxy(network.Side side) {
		this.side = side;
	}

	/** debug时作为记录，放置init重复运行 */
	static boolean hasRun = false;

	/** 初始化 */
	public void init() {
		if (hasRun)
			return;
		hasRun = true;
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
		// 注册消息
		Proxy.registerAllMesage();
		// Story注册
		Proxy.registerAllStory();
	}

	/** 启动 */
	public void launch() {
	}

	private static void registerAllMesage() {
		RecvDealMessage.registerMessage("login_msh", MessageLogin.class);
		RecvDealMessage.registerMessage("us_str", MUSString.class);
		RecvDealMessage.registerMessage("ul_change", MUGULRequest.class);
		RecvDealMessage.registerMessage("msbegin", MSBegin.class);
		RecvDealMessage.registerMessage("msend", MSEnd.class);
		RecvDealMessage.registerMessage("msmem", MSMemberIn.class);
		RecvDealMessage.registerMessage("msmsend", MSNbtSend.class);
		RecvDealMessage.registerMessage("file_send", UMFileSendToUser.class);
		RecvDealMessage.registerMessage("user_search", MUSSearch.class);
	}

	private static void registerAllStory() {
		Story.registerStory("fileSender", StoryFileSender.class);
	}
}
