package user.message;

import java.util.ArrayList;

import client.event.EventsBridge;
import event.EventBus;
import log.Logger;
import nbt.NBTBase;
import nbt.NBTTagCompound;
import nbt.NBTTagList;
import nbt.NBTTagString;
import user.User;

public class MUGULRequest extends MessageUser {
//用户请求好友列表
public MUGULRequest() {}
public MUGULRequest(User user) 
{
	super(user);
	nbt.setString("user name", user.userName);
}
public MUGULRequest(User user,NBTTagCompound nbt) 
{
	super(user);
	this.nbt=nbt;
	nbt.setString("user name", user.userName);
}
	@Override
	void executeClient(User from, NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		if(nbt.getTag("userlist").getId()==NBTBase.TAG_LIST)
		{
			NBTTagList list=(NBTTagList)nbt.getTag("userlist");
			if(list.getTagType()==NBTBase.TAG_STRING)
			{
				ArrayList<User>ul=new ArrayList<User>();
				for(int i=0;i<list.size();i++)
				{
					NBTTagString s=(NBTTagString)list.get(i);
					ul.add(new User(s.get()));
				}
				EventsBridge.recvUserList(ul);
			}
			else
			{
				//报错
				;
			}			
		}
		else
		{
			//报错
			;
		}
	}

	@Override
	void executeServer(User from, User to, NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		//测试getuserlist()
		String[] s= new String[]{"hh","ll","ssj"};
		
		NBTTagList list=new NBTTagList();
		Logger.log.impart("服务器成功响应");
		for(int i=0;i<s.length;i++)
		{
			list.appendTag(new NBTTagString(s[i]));
		}
		nbt.setTag("userlist", list);
		if (to.isOnline()) {
			to.sendMesage(new MUGULRequest(from,nbt));
		} else {
			Logger.log.impart(from,"在接收回复前掉线了");
		}
	}

}
