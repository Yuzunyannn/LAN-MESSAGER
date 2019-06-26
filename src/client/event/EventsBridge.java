package client.event;

import java.io.File;
import java.util.ArrayList;

import client.user.UserClient;
import event.EventBusTask;
import event.IEventBus;
import event.SubscribeEvent;
import log.Logger;
import transfer.FileSenderManager;
import transfer.message.UMFileSendToUser;
import user.User;
import user.UserSpecial;
import user.message.MUSString;
import user.message.MessageGroupCreate;

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

	/** [后台]当受到消息 */
	public static void recvString(User from, UserSpecial sp, String info) {
		EventsBridge.frontendEventHandle.post(new EventRecv.EventRecvString(from, sp, info));
	}

	/** [后台]用户上线或下线 */
	public static void userLoginSate(User user, boolean isOnline) {
		EventsBridge.frontendEventHandle
				.post(new EventLogState(isOnline ? EventLogState.ONLINE : EventLogState.OFFLINE));
	}

	/** [前台] 发送字符串给其他用户 */
	public static void sendString(String str, User toUser) {
		UserClient.sendToServer(new MUSString(toUser, str));
	}

	/** [前台] 发送文件给其他用户 */
	public static void sendFile(File file, User toUser) {
		int key = FileSenderManager.recordFile(file);
		if (key == 0) {
			Logger.log.warn("发送文件失败！");
			return;
		}
		UserClient.sendToServer(new UMFileSendToUser(toUser, key, file.getName()));
	}

	/** [后台]当收到用户列表 */
	public static void recvUserList(ArrayList<User> ul) {
		EventsBridge.frontendEventHandle.post(new EventULChange(ul, EventULChange.ADD));
	}

	/** 发送建立群组消息 */
	public static void groupCreateRequest(MessageGroupCreate group) {
		UserClient.sendToServer(group);
	}

//	/**[后台]收到搜索结果*/
//	public static void recvSearchRequest(ArrayList<User> sul) {
//		EventsBridge.frontendEventHandle.post(new EventSearchRequest(sul));
//	}
	// 转发事件
	@SubscribeEvent
	public static void retransmissionRecv(transfer.EventFileRecv.Start e) {
		EventsBridge.frontendEventHandle.post(e);
	}

	// 转发事件
	@SubscribeEvent
	public static void retransmissionRecv(transfer.EventFileRecv.Finish e) {
		EventsBridge.frontendEventHandle.post(e);
	}

	// 转发事件
	@SubscribeEvent
	public static void retransmissionSend(transfer.EventFileSend.Start e) {
		EventsBridge.frontendEventHandle.post(e);
	}

	// 转发事件
	@SubscribeEvent
	public static void retransmissionRev(transfer.EventFileSend.Finish e) {
		EventsBridge.frontendEventHandle.post(e);
	}

}
