package server.user;

import event.SubscribeEvent;
import log.Logger;
import network.IMessage;
import network.RecvDealMessage;
import network.event.EventValidationSuccess;
import user.message.MessageLogin;

public class ULogin extends RecvDealMessage {

	/** 校验成功后，设置处理模式 */
	@SubscribeEvent
	public static void toWaitLogin(EventValidationSuccess event) {
		if (event.isClient())
			return;
		event.con.setRecvDeal(new ULogin());
	}

	/** 分离消息，确保是登录消息 */
	@Override
	protected void execute(IMessage msg, network.Connection con) {
		if (!(msg instanceof MessageLogin)) {
			Logger.log.warn("连接" + con + "未登录，却发送了别的报文！");
			return;
		}
		super.execute(msg, con);
	}

	static java.sql.Connection con = null;

	/** 处理登陆 */
	static public boolean login(String username, String password) {
		return ULogin.debug(username, password);
//		boolean result = false;
//		try {
//			if (con == null || con.isClosed())
//				con = Database.connect();
//			Statement statement = con.createStatement();
//			ResultSet rs = statement.executeQuery("select u_password from User where u_name='" + username + "'");
//			while (rs.next()) {
//				String psw = rs.getString("u_password");
//				if (psw.equals(password))
//					result = true;
//			}
//			rs.close();
//			statement.close();
//		} catch (SQLException e) {
//			Logger.log.error("数据库链接失败！", e);
//		}
//		return result;
	}

	static private boolean debug(String username, String password) {
		if (username.equals("guest")) {
			return true;
		} else if (username.equals("ssj") && password.equals("123")) {
			return true;
		} else if (username.equals("ycy") && password.equals("tatsuu")) {
			return true;
		} else if (username.equals("lyl") && password.equals("666")) {
			return true;
		} else if (username.equals("myk") && password.equals("yyy")) {
			return true;
		}
		return false;
	}
}
