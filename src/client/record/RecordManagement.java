package client.record;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import core.Core;
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

	@Override
	public int update() {
		tick++;
		if (tick % (20 * 5) == 0) {
			Iterator<Entry<User, Record>> iter = records.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<User, Record> entry = iter.next();
				Record rec = entry.getValue();
				rec.save();
			}
		}
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

}
