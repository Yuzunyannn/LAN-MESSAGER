package core;

import client.event.FrontendEvents;

/** 程序后台和界面交互 [A]表示由A调用 */
public class EventsBridge {

	/** [后台] */
	public static void login(String recvUsername, String info) {
		FrontendEvents.eventHandle.post(new client.event.EventLogin(recvUsername, info));
	}
}
