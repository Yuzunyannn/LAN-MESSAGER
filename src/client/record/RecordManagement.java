package client.record;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import client.event.EventNeedSave;
import client.user.UserClient;
import core.Core;
import event.SubscribeEvent;
import nbt.NBTTagCompound;
import resmgt.ResourceInfo;
import resmgt.ResourceManagement;
import story.ITickable;
import user.User;

public class RecordManagement implements ITickable {

	static {
		Core.task(new RecordManagement());
	}

	static private Map<User, Record> records = new HashMap<User, Record>();
	static long tick = 0;

	/** 获取用户本地记录 */
	static public Record getRecord(User user) {
		if (!records.containsKey(user))
			records.put(user, new Record(user));
		return records.get(user);
	}

	/** 获取登陆记录信息 */
	static public RecordLogInfo getLogInfo() {
		ResourceInfo rinfo = ResourceManagement.instance.loadOrCreateTmpNBT("g357o85l", "loginfo");
		NBTTagCompound nbt = rinfo.getNBT();
		RecordLogInfo info = new RecordLogInfo();
		info.username = nbt.getString("unam");
		info.password = nbt.getString("paw");
		return info;
	}

	/** 记录登陆信息 */
	static public void setLogInfo(RecordLogInfo info) {
		ResourceInfo rinfo = ResourceManagement.instance.loadOrCreateTmpNBT("g357o85l", "loginfo");
		NBTTagCompound nbt = rinfo.getNBT();
		nbt.setString("unam", info.username);
		nbt.setString("paw", info.password);
		rinfo.save();
	}

	@Override
	public int update() {
		if (UserClient.toServer == null)
			return ITickable.SUCCESS;
		tick++;
		if (tick % (20 * 5) == 0)
			saveOnce();
		if (tick % (20 * 60) == 0) {
			Iterator<Entry<User, Record>> iter = records.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<User, Record> entry = iter.next();
				Record rec = entry.getValue();
				if (rec.release()) {
					iter.remove();
				}
			}
		}
		return ITickable.SUCCESS;
	}
	/** 进行一次保存 */
	private static void saveOnce() {
		Iterator<Entry<User, Record>> iter = records.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<User, Record> entry = iter.next();
			Record rec = entry.getValue();
			rec.save();
		}
	}
	
	@SubscribeEvent
	public static void onClose(EventNeedSave e) {
		saveOnce();
	}

}
