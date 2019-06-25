package user;

public class UserSpecial {
	final String specialName;

	public UserSpecial(String specialName) {
		this.specialName = specialName;
	}

	public UserSpecial(User user) {
		this.specialName = user.getUserName();
	}

	/** 获取特殊虚拟用户类型 */
	public UserSpecialType getType() {
		if (specialName.charAt(0) != '#')
			return UserSpecialType.NONE;
		if (specialName.length() < 2)
			return UserSpecialType.NONE;
		switch (specialName.charAt(1)) {
		case 'G':
			return UserSpecialType.GROUP;
		}
		return UserSpecialType.NONE;
	}
}
