package server.user;

import network.RecvDealMessage;
import user.User;

public class USearch extends RecvDealMessage {

	public User searchName(String user) {
		return new User(user);
		};
}
