package client.event;

import client.user.UserClient;
import event.EventBusTask;
import event.IEventBus;
import user.User;
import user.message.MUSString;

/** 程序后台和界面交互(只有Client分前台和后台) [A]表示由A调用 */
public class EventsBridge {

	public static final IEventBus fontendEventHandle = new EventBusTask();

	/** [后台] */
	public static void login(String recvUsername, String info) {
		EventsBridge.fontendEventHandle.post(new client.event.EventLogin(recvUsername, info));
	}

	/** [后台] */
	public static void recvString(User from, String info) {
		EventsBridge.fontendEventHandle.post(new EventRecv.EventRecvString(from, info));
	}

	/** [前台] */
	public static void sendString(String str, String toUser) {
		UserClient.sendToServer(new MUSString(toUser, str));
	}
}
