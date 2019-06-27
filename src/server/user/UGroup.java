package server.user;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import log.Logger;
import nbt.NBTBase;
import nbt.NBTTagCompound;
import resmgt.ResourceInfo;
import resmgt.ResourceManagement;
import user.UOnline;
import user.User;
import user.UserSpecial;
import user.message.MUSString;
import user.message.MessageGroupInfo;

public class UGroup {

	static public void createGroup(User from, List<User> users) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("boss", from.getUserName());
		nbt.setLong("time", System.currentTimeMillis());
		for (int i = 0; i < users.size(); i++) {
			User member = users.get(i);
			if (!member.isExist()) {
				Logger.log.warn("用户" + member + "不存在！");
				continue;
			}
			nbt.setBoolean(member.getUserName(), true);
		}
		String groupId = UGroup.randomGruopNumber("G" + (short) from.getUserName().charAt(0));
		if (ResourceManagement.instance.hasDataResource("groups/" + groupId)) {
			Logger.log.warn("拥有重复的组，不能继续创建！");
			return;
		}
		ResourceInfo info = ResourceManagement.instance.loadOrCreateDataResource("groups/" + groupId);
		info.setData(nbt);
		info.save();
		Logger.log.impart("群" + groupId + "创建成功！");
		UGroup.tellUser(new UserSpecial("#" + groupId), from);
	}

	static public void msgToGruop(User from, UserSpecial sp, String str) {
		String groupId = sp.getId();
		NBTTagCompound nbt = UGroup.getGroupNBT(groupId);
		if (nbt == null)
			return;
		for (Entry<String, NBTBase> entry : nbt) {
			NBTBase base = entry.getValue();
			if (base.getId() == NBTBase.TAG_BYTE) {
				User user = UOnline.getInstance().getUser(entry.getKey());
				if (user.equals(from))
					continue;
				user.sendMesage(new MUSString(from, sp, str));
			}
		}
	}

	static public void tellUser(UserSpecial sp, User to) {
		String groupId = sp.getId();
		NBTTagCompound nbt = UGroup.getGroupNBT(groupId);
		if (nbt == null)
			return;
		List<User> users = new LinkedList<>();
		for (Entry<String, NBTBase> entry : nbt) {
			NBTBase base = entry.getValue();
			if (base.getId() == NBTBase.TAG_BYTE) {
				User user = UOnline.getInstance().getUser(entry.getKey());
				users.add(user);
			}
		}
		User boss = UOnline.getInstance().getUser(nbt.getString("boss"));
		to.sendMesage(new MessageGroupInfo(sp, boss, users));
	}

	static private NBTTagCompound getGroupNBT(String groupId) {
		ResourceInfo info = ResourceManagement.instance.loadDataResource("groups/" + groupId);
		if (info == null) {
			Logger.log.warn("不存在的群！" + groupId);
			return null;
		}
		NBTTagCompound nbt = info.getNBT();
		if (nbt == null) {
			Logger.log.warn("群的数据不存在！" + groupId);
			return null;
		}
		return nbt;
	}

	/** 随机获取一个组号 */
	static String randomGruopNumber(String head) {
		return head + System.currentTimeMillis();
	}

}
