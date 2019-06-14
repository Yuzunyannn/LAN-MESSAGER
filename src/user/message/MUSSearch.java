package user.message;

import client.event.EventsBridge;
import log.Logger;
import nbt.NBTBase;
import nbt.NBTTagCompound;
import server.user.USearch;
import user.User;

public class MUSSearch extends MessageUser {
	public MUSSearch() {}
	public MUSSearch(User user,String sname) {
		super(user);
		NBTTagCompound nbt1=new NBTTagCompound();
		nbt1.setString("searchName", sname);
		nbt.setTag("search", nbt1);
	}

	@Override
	protected void executeClient(User from, NBTTagCompound nbt) {
		EventsBridge.recvString(from, nbt.getString("search"));
		
	}

	@Override
	protected void executeServer(User from, User to, NBTTagCompound nbt) {
		if(!from.equals(to))
			Logger.log.error("请求回应的对象错误");
		USearch search=new USearch();
		User str=search.searchName(((NBTTagCompound)nbt.getTag("search")).getString("searchName"));
		if (to.isOnline()) {
			to.sendMesage(new MUSString(from,str.toString()));
		} else {
			Logger.log.impart("请求用户掉线，请求失效");
		}
		

	}

}
