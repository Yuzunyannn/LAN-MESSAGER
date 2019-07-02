package server.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import log.Logger;
import user.SearchHelper;
import user.UOnline;
import user.User;
import user.message.MUSSearch;

public class USearch implements Runnable {
	private LinkedList<User> list = new LinkedList<User>();
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
		SearchHelper sh = new SearchHelper();
		list = (LinkedList<User>) sh.search(UOnline.getInstance().getOnlineUsers(), name);
		if (list.isEmpty()) {
			System.out.println("search helper 没有搜到结果");
			return false;
		}
		MUSSearch msgsearch = new MUSSearch(new ArrayList(list), name);
		user.sendMesage(msgsearch);
		return true;

	}
}
