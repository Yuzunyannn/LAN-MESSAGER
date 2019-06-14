package user;

import network.IMessage;

abstract public class User {

	public static final User EMPTY = new User("") {
	};

	/** 用户的唯一表示名称 */
	public final String userName;

	public User(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return userName;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public int hashCode() {
		return userName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof User))
			return false;
		return userName.equals(((User) obj).userName);
	}

	/** 发送消息 */
	public boolean sendMesage(IMessage msg) {
		return false;
	}

	/** 是否在线 */
	public boolean isOnline() {
		return true;
	}
}
