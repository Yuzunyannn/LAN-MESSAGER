package user.message;

import java.util.ArrayList;

import client.event.EventsBridge;
import client.user.UserClient;
import core.Core;
import log.Logger;
import nbt.NBTBase;
import nbt.NBTTagCompound;
import nbt.NBTTagList;
import nbt.NBTTagString;
import server.response.Response;
import user.User;

public class MUGULRequest extends MessageUser {
	// 用户请求好友列表
	public MUGULRequest() {
	}

	public MUGULRequest(User user) {
		super(user);
	}
	
//	public MUGULRequest(User user,int size,int id) {
//		super(user,size,id);
//	}
	@Override
	protected void executeClient(User from, NBTTagCompound nbt) {
		if (nbt.getTag("userlist").getId() == NBTBase.TAG_LIST) {
			NBTTagList list = (NBTTagList) nbt.getTag("userlist");
			if (list.getTagType() == NBTBase.TAG_STRING) {
				ArrayList<User> ul = new ArrayList<User>();
				for (int i = 0; i < list.size(); i++) {
					NBTTagString s = (NBTTagString) list.get(i);
					ul.add(new UserClient(s.get()));
				}
				EventsBridge.recvUserList(ul);
			} else
				Logger.log.warn("获取好友列表的list不是字符串型的list！");
		} else
			Logger.log.warn("获取好友列表的类型不对！");

	}

	@Override
	protected void executeServer(User from, User to, NBTTagCompound nbt) {
		Core.task(new Response(from));
	}

}
