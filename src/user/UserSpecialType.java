package user;

public enum UserSpecialType {
	NONE("未知"), GROUP("群");
	final String str;

	UserSpecialType(String name) {
		this.str = name;
	}

	@Override
	public String toString() {
		return this.str;
	}
}
