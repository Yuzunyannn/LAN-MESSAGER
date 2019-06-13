package user.message;

import java.util.ArrayList;

import client.event.EventsBridge;
import client.user.UserClient;
import core.Core;
import nbt.NBTBase;
import nbt.NBTTagCompound;
import nbt.NBTTagList;
import nbt.NBTTagString;
import server.response.Response;
import user.User;

public class MUGULRequest extends MessageUser {
//用户请求好友列表
	public MUGULRequest() {
	}

	public MUGULRequest(User user) {
		super(user);
	}

	@Override
	void executeClient(User from, NBTTagCompound nbt) {
		System.out.println(nbt.getTag("userlist"));
		if (nbt.getTag("userlist").getId() == NBTBase.TAG_LIST) {
			NBTTagList list = (NBTTagList) nbt.getTag("userlist");
			if (list.getTagType() == NBTBase.TAG_STRING) {
				ArrayList<User> ul = new ArrayList<User>();
				for (int i = 0; i < list.size(); i++) {
					NBTTagString s = (NBTTagString) list.get(i);
					ul.add(new UserClient(s.get()));
				}
				EventsBridge.recvUserList(ul);
			} else {
				// 报错
				;
			}
		} else {
			// 报错
			;
		}
	}

	@Override
	void executeServer(User from, User to, NBTTagCompound nbt) {
		// 测试getuserlist()
		Core.task(new Response(from));	
	}

}
