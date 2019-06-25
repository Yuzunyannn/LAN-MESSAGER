package user.message;

import java.util.ArrayList;

import client.event.EventSearchRequest;
import client.event.EventsBridge;
import client.user.UserClient;
import core.Core;
import log.Logger;
import nbt.NBTBase;
import nbt.NBTTagCompound;
import nbt.NBTTagList;
import nbt.NBTTagString;
import server.user.USearch;
import user.User;

public class MUSSearch extends MessageUser {
	public MUSSearch() {
	}
	/**
	 * @param name 发起请求的用户的名字*/
	public MUSSearch(String name) {
		nbt.setString("searchline", name);
	}
	/**
	 * @param ul */
	public MUSSearch(ArrayList<User> ul,String searchline) {

		NBTTagList list = new NBTTagList();
		for (User str : ul)
			list.appendTag(str.getUserName());
		NBTTagCompound nbt1 = new NBTTagCompound();
		nbt1.setTag("searchList", list);
		nbt1.setString("searchline", searchline);
		nbt.setTag("search", nbt1);
	}

	@Override
	protected void executeClient(User from, NBTTagCompound nbt) {
		NBTTagCompound nbt1=nbt.getCompoundTag("search");
		if (nbt1.getTag("searchList").getId() == NBTBase.TAG_LIST) {
			NBTTagList list = (NBTTagList) nbt1.getTag("searchList");
			if (list.getTagType() == NBTBase.TAG_STRING) {
				ArrayList<User> ul = new ArrayList<User>();
				for (int i = 0; i < list.size(); i++) {
					NBTTagString s = (NBTTagString) list.get(i);
					ul.add(new UserClient(s.get()));
				}
				EventsBridge.frontendEventHandle.post(new EventSearchRequest( ul));
			} else
				Logger.log.warn("获取搜索列表中的数据不是字符串型！");
		} else
			Logger.log.warn("获取搜索列表的类型不是列表！");

	}

	@Override
	protected void executeServer(User from, User to, NBTTagCompound nbt) {
		if(nbt.getTag("searchline").getId()!=NBTBase.TAG_STRING) {
			Logger.log.warn("没有成功得到搜索框中的内容");
			return;
			}
		String searchline=nbt.getString("searchline");	
			Core.task(new USearch(searchline, from));

			


	}

}
