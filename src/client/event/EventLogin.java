package client.event;

import user.UOnline;

/** 当用户登陆结果返回 */
public class EventLogin extends EventRecv {

	// 返回的登陆结果
	public final String info;
	// 返回的用户名
	public final String username;

	public EventLogin(String username, String info) {
		super(UOnline.getInstance().getUser(username));
		this.username = username;
		this.info = info;
	}
}
