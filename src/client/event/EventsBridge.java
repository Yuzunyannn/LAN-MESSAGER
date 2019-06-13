package client.event;

import java.util.ArrayList;

import client.user.UserClient;
import event.EventBusTask;
import event.IEventBus;
import user.User;
import user.message.MUSString;

/** 程序后台和界面交互(只有Client分前台和后台) [A]表示由A调用 */
public class EventsBridge {

	public static final IEventBus frontendEventHandle = new EventBusTask();

	/** [后台] 当登陆结果返回时的处理 */
	public static void login(String recvUsername, String info) {
		EventsBridge.frontendEventHandle.post(new client.event.EventLogin(recvUsername, info));
	}

	/** [后台]当受到消息 */
	public static void recvString(User from, String info) {
		EventsBridge.frontendEventHandle.post(new EventRecv.EventRecvString(from, info));
	}

	/** [前台] 发送字符串给其他用户 */
	public static void sendString(String str, String toUser) {
		UserClient.sendToServer(new MUSString(toUser, str));
	}
	/** [后台]当受到用户列表 */
	public static void recvUserList(ArrayList<User>ul) {
		EventsBridge.frontendEventHandle.post(new EventULChange(ul));
	}
}
