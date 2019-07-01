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
import event.SubscribeEvent;
import log.Logger;
import nbt.NBTBase;
import nbt.NBTTagCompound;
import resmgt.ResourceInfo;
import resmgt.ResourceManagement;
import server.event.EventHour;
import util.FileHelper;

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
			if (k.getKey().equals(key))
				return true;
		}
		return false;
	}

	static public void serverFinishRecv(File file) {
		String date = FileHelper.getDateString();
		ResourceInfo info = ResourceManagement.instance.loadOrCreateTmpResource("file_" + date, "file_" + date);
		NBTTagCompound nbt = info.getNBT();
		if (nbt == null) {
			info.setData(new NBTTagCompound());
			nbt = info.getNBT();
		}
		nbt.setBoolean(file.getName(), true);
		info.save();
	}
	
	@SubscribeEvent
	public static void onHourChange(EventHour e) {
		if (e.newDay()) {
			Logger.log.impart("新的一天到来了！File文件管理正在运行！");
			FileSenderManager.clearOnce();
		}
	}

	static public void clearOnce() {
		String date = FileHelper.getDateString(-5);
		ResourceInfo info = ResourceManagement.instance.loadTmpResource("file_" + date, "file_" + date);
		if (info != null) {
			if (!info.isLoaded())
				info.load();
			if (info.getType() != ResourceInfo.Type.NBT) {
				Logger.log.warn("读取到的File记录临时文件数据的数据类型错误！");
				return;
			}
			
			NBTTagCompound nbt = info.getNBT();
			for (Entry<String, NBTBase> entry : nbt) {
				String str = entry.getKey();
				File file = new File("./tmp" + str);
				if (file.exists()) {
					if (file.delete() == false) {
						Logger.log.warn("临时文件(" + file.getName() + ")清理失败！");
					}
				}
			}
		}
		info.release();
		info.getFile().delete();
	}

}
