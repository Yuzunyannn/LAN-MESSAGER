package transfer;

import java.io.File;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import event.EventBusSynchronized;
import event.IEventBus;

public class FileSenderManager {

	/** 文件发送事件 */
	final public static IEventBus eventHandle = new EventBusSynchronized();
	/** 记录发送文件的key */
	final private static List<Entry<Integer, File>> keys = new LinkedList<Entry<Integer, File>>();

	static final Random rand = new Random();

	static public File getFileWithKey(int key) {
		Iterator<Entry<Integer, File>> itr = keys.iterator();
		while (itr.hasNext()) {
			Entry<Integer, File> entry = itr.next();
			if (entry.getKey().equals(key)) {
				itr.remove();
				return entry.getValue();
			}
		}
		return null;
	}

	static public int recordFile(File file) {
		if (!file.exists())
			return 0;
		int key = 0;
		int i = 0;
		do {
			i++;
			if (i > 20)
				return 0;
			key = rand.nextInt();
		} while (hasKey(key) || key == 0);
		keys.add(new AbstractMap.SimpleEntry<>(key, file));
		return key;
	}

	static public boolean hasKey(int key) {
		for (Entry<Integer, File> k : keys) {
			if (k.getValue().equals(key))
				return true;
		}
		return false;
	}

}
