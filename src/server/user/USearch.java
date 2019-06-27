package server.user;

import java.util.ArrayList;
import log.Logger;
import user.UOnline;
import user.User;
import user.message.MUSSearch;

public class USearch implements Runnable {
	private ArrayList<User> list = new ArrayList<User>();
	private String name;
	private User user;

	public USearch(String name, User user) {
		this.name = name;
		this.user = user;
	}

	@Override
	public void run() {
		if (search(name))
			Logger.log.impart("成功搜索");
		else
			Logger.log.impart("搜索失败");

	};

	private boolean search(String name) {
		String str[] = { "1234", "123", "12", "1" };
		if (str.length == 0)
			return false;
		for (int i = 0; i < str.length; i++)
			list.add(UOnline.getInstance().getUser(str[i]));
		MUSSearch msgsearch = new MUSSearch(list, name);
		user.sendMesage(msgsearch);
		return true;

	}
}
