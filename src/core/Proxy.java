package core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import log.Logger;
import log.NeedLog;
import network.RecvDealMessage;
import story.Story;
import story.message.MSBegin;
import story.message.MSEnd;
import story.message.MSMemberIn;
import story.message.MSNbtSend;
import story.message.MSResult;
import transfer.StoryFileSender;
import transfer.message.UMFileSendToUser;
import user.message.MUGULRequest;
import user.message.MUSSearch;
import user.message.MUSString;
import user.message.MessageEmergency;
import user.message.MessageGroupCreate;
import user.message.MessageGroupInfo;
import user.message.MessageHasRead;
import user.message.MessageLogin;
import user.message.MessageUserStateChange;
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
				Field[] fields;
				try {
					fields = cls.getDeclaredFields();
				} catch (NoClassDefFoundError e) {
					Logger.log.warn("指定类的变量无法被运行加载:" + cls);
					continue;
				}
				for (Field field : fields) {
					if (!Modifier.isStatic(field.getModifiers()))
						continue;
					// 为所有需要log的类赋值
					if (field.getAnnotation(NeedLog.class) != null) {
						field.set(null, Logger.log);
					}
				}
			}
		} catch (Throwable e) {
			Logger.log.error("反射类，初始化时出现问题！", e);
			Logger.log.error("请注意！！！！");
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
		RecvDealMessage.registerMessage("msresult", MSResult.class);
		RecvDealMessage.registerMessage("msend", MSEnd.class);
		RecvDealMessage.registerMessage("msmem", MSMemberIn.class);
		RecvDealMessage.registerMessage("msmsend", MSNbtSend.class);
		RecvDealMessage.registerMessage("file_send", UMFileSendToUser.class);
		RecvDealMessage.registerMessage("user_search", MUSSearch.class);
		RecvDealMessage.registerMessage("usate_change", MessageUserStateChange.class);
		RecvDealMessage.registerMessage("ugroup_create", MessageGroupCreate.class);
		RecvDealMessage.registerMessage("ugroup_info", MessageGroupInfo.class);
		RecvDealMessage.registerMessage("emergency", MessageEmergency.class);
		RecvDealMessage.registerMessage("m_has_read", MessageHasRead.class);
	}

	// 注册一个story
	private static void registerAllStory() {
		Story.registerStory("fileSender", StoryFileSender.class);
	}
}
