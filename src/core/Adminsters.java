package core;

public class Adminsters {

	static public String userToInfo(String str) {
		switch (str) {
		case "yuzunyannn":
			str = "管理员：李";
			break;
		case "Bevis":
			str = "管理员：沈";
			break;
		case "tatsuu":
			str = "管理员：闫";
			break;
		case "dispute":
			str = "管理员：孟";
			break;
		default:
			break;
		}
		return str;
	}
}
