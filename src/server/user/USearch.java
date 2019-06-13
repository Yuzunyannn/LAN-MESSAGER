package server.user;

import network.RecvDealMessage;
import user.UOnline;
import user.User;

public class USearch extends RecvDealMessage {

	public User searchName(String user) {
		return UOnline.getInstance().getUser(user);
		};
}
