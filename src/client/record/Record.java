package client.record;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import client.user.UserClient;
import client.word.Word;
import client.word.WordString;
import log.Logger;
import nbt.NBTBase;
import nbt.NBTTagCompound;
import nbt.NBTTagList;
import nbt.NBTTagString;
import resmgt.ResourceInfo;
import resmgt.ResourceManagement;
import user.UOnline;
import user.User;

public class Record {

	static public String FILEMARK = "#FILE?:";
	static public String FILEEND = ":?FILEEND#";
	static public String MEMEMARK = "#MEME?:";
	static public String TIMEMARK = "#TIME?:";

	static private String toRecordString(String str) {
		StringBuffer out = new StringBuffer("");
		for (int i = 0; i < str.length(); i++) {
			out.append((short) str.charAt(i));
		}
		return out.toString();
	}

	/** 获取本地用户聊天对象记录 */
	static public List<String> getLocalChatToList(String thisUser) {
		List<String> userChatToList = new ArrayList<String>();
		ResourceInfo info = ResourceManagement.instance.loadOrCreateTmpNBT("ulist/" + thisUser, "ulist/" + thisUser);
		NBTTagCompound nbt = info.getNBT();
		if (!nbt.hasKey("ulist")) {
			nbt.setTag("ulist", new NBTTagList());
		}
		NBTTagList list = (NBTTagList) nbt.getTag("ulist");
		for (NBTBase nbtBase : list) {
			userChatToList.add(((NBTTagString)nbtBase).toString());
		}
		return userChatToList;
	}

	/** 删除一个聊天对象 */
	static public void deleteUserFile(String thisUser, String addUser) {
		ResourceInfo info = ResourceManagement.instance.loadOrCreateTmpNBT("ulist/" + thisUser, "ulist/" + thisUser);
		NBTTagCompound nbt = info.getNBT();
		if (!nbt.hasKey("ulist")) {
			nbt.setTag("ulist", new NBTTagList());
		}
		NBTTagList list = (NBTTagList) nbt.getTag("ulist");
		Iterator<NBTBase> iter = list.iterator();
		while(iter.hasNext()) {
			NBTTagString str = (NBTTagString) iter.next();
			if(str.get().equals(addUser)) {
				iter.remove();
				break;
			}
		}
		info.save();
	}

	/** 添加一个聊天对象 */
	static public void updateUserFile(String thisUser, String addUser) {
		ResourceInfo info = ResourceManagement.instance.loadOrCreateTmpNBT("ulist/" + thisUser, "ulist/" + thisUser);
		NBTTagCompound nbt = info.getNBT();
		if (!nbt.hasKey("ulist")) {
			nbt.setTag("ulist", new NBTTagList());
		}
		NBTTagList list = (NBTTagList) nbt.getTag("ulist");
		boolean has = false;
		for(NBTBase base : list) {
			NBTTagString str = (NBTTagString) base;
			if(str.get().equals(addUser)) {
				has = true;
				break;
			}
		}
		if (!has) {
			list.appendTag(addUser);
			info.save();
		}
	}

	public static boolean writeTxtFile(File fileName, List<String> addUser) {
		boolean flag = false;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(fileName);
			for (String string : addUser) {
				fileOutputStream.write(string.getBytes("UTF-8"));
				fileOutputStream.write("\n".getBytes("UTF-8"));
			}
			fileOutputStream.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 删除一个文件夹以这个文件夹下的子目录 */
	/***
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path 文件夹完整绝对路径
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/***
	 * 删除文件夹
	 * 
	 * @param folderPath文件夹完整绝对路径
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
			Logger.log.impart("聊天记录删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log.impart("聊天记录删除异常");
		}
	}

	/** 删除一个对象的聊天记录 */
	static public void deleteRecord(String chatTo) {
//		Record rec = RecordManagement.getRecord(UOnline.getInstance().getUser(chatTo));
//		Logger.log.impart("正在删除" + chatTo + "的聊天记录");
//		delFolder("./tmp/" + root);
	}

	static class RecInfo {
		LinkedList<RecordValue> words = new LinkedList<RecordValue>();
		NBTTagList nbtList = new NBTTagList();
		boolean isChange = false;
		int year, month, day;
		long tick = 0;
	}

	private NBTTagCompound data;
	private final User user;
	private Map<String, RecInfo> maps = new TreeMap<String, RecInfo>();

	public Record(User user) {
		this.user = user;
		ResourceManagement.mkdirTmp();
		String root = this.getRoot();
		ResourceInfo rinfo = ResourceManagement.instance.loadOrCreateTmpNBT(root + "/info",
				this.user.getUserName() + "/info");
		data = rinfo.getNBT();
	}

	RecInfo getWords(int year, int month, int day) {
		String timeStr = year + "-" + month + "-" + day;
		if (maps.containsKey(timeStr)) {
			RecInfo info = maps.get(timeStr);
			info.tick = RecordManagement.tick;
			return info;
		}
		ResourceManagement.mkdirTmp();
		RecInfo info = new RecInfo();
		info.year = year;
		info.month = month;
		info.day = day;
		ResourceInfo rinfo = getRes(info);
		if (rinfo == null) {
			Logger.log.warn("客户端创建聊天记录文件异常！");
			return info;
		}
		if (rinfo.getType() != ResourceInfo.Type.NBT) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setTag("list", info.nbtList);
			rinfo.setData(nbt);
		} else {
			NBTTagCompound nbt = rinfo.getNBT();
			info.nbtList = (NBTTagList) nbt.getTag("list");
			for (NBTBase base : info.nbtList) {
				NBTTagCompound nyo = (NBTTagCompound) base;
				long time = nyo.getLong("time");
				String str = nyo.getString("str");
				String un = nyo.getString("who");
				Word word = new WordString(str, time);
				User who = UOnline.getInstance().getUser(un);
				info.words.addLast(new RecordValue(word, who));
			}
		}
		info.tick = RecordManagement.tick;
		maps.put(timeStr, info);
		this.update(timeStr);
		return info;
	}

	// 更新用户数据
	public void update(String timeStr) {
		this.data.setBoolean(timeStr, true);
		String lastTimeStr = this.data.getString("last");
		if (lastTimeStr.isEmpty()) {
			this.data.setString("last", timeStr);
		} else if (lastTimeStr.compareTo(timeStr) < 0) {
			this.data.setString("last", timeStr);
		}
	}

	/** 添加一条新纪录 */
	public void addNew(Word word, User whos) {
		System.out.println("getPic lalalala :" + word.getValue());
		Calendar cal = Calendar.getInstance();
		RecInfo info = this.getWords(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("time", word.time);
		nbt.setString("str", word.toString());
		nbt.setString("who", whos.getUserName());
		info.nbtList.appendTag(nbt);
		info.words.addLast(new RecordValue(word, whos));
		info.isChange = true;
	}

	/** 获取最后一次记录距今间隔 */
	public int getLastDayOff() {
		String last = this.data.getString("last");
		String[] time = last.split("-");
		if (time == null || time.length < 3)
			return 0;
		int year = Integer.parseInt(time[0]);
		int month = Integer.parseInt(time[1]);
		int day = Integer.parseInt(time[2]);
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, year);
		date.set(Calendar.MONTH, month);
		date.set(Calendar.DATE, day);
		return daysBetween(Calendar.getInstance(), date);
	}

	private static final int daysBetween(Calendar early, Calendar late) {
		java.util.Calendar calst = early;
		java.util.Calendar caled = late;
		calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
		calst.set(java.util.Calendar.MINUTE, 0);
		calst.set(java.util.Calendar.SECOND, 0);
		caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
		caled.set(java.util.Calendar.MINUTE, 0);
		caled.set(java.util.Calendar.SECOND, 0);
		// 得到两个日期相差的天数
		int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
		return days;
	}

	/** 获取从现在开始的的相对天数的聊天记录 */
	public List<RecordValue> getRecordValues(int offday) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, offday);
		RecInfo info = this.getWords(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
		return info.words;
	}

	/** 保存 */
	public void save() {
		for (RecInfo info : maps.values()) {
			if (info.isChange) {
				info.isChange = false;
				ResourceInfo rinfo = getRes(info);
				NBTTagCompound nbt = rinfo.getNBT();
				if (nbt == null) {
					nbt = new NBTTagCompound();
					rinfo.setData(nbt);
				}
				nbt.setTag("list", info.nbtList);
				rinfo.save();
			}
		}
		String root = this.getRoot();
		ResourceInfo rinfo = ResourceManagement.instance.loadOrCreateTmpResource(root + "/info",
				this.user.getUserName() + "/info");
		rinfo.setData(data);
		rinfo.save();
	}

	/** 释放 */
	public boolean release() {
		Iterator<Entry<String, RecInfo>> iter = maps.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, RecInfo> entry = iter.next();
			RecInfo info = entry.getValue();
			if (RecordManagement.tick - info.tick > 20 * 60 * 5) {
				ResourceInfo rinfo = getRes(info);
				rinfo.release();
				iter.remove();
			}
		}
		if (maps.size() == 0) {
			String root = this.getRoot();
			ResourceInfo rinfo = ResourceManagement.instance.loadOrCreateTmpResource(root + "/info",
					this.user.getUserName() + "/info");
			rinfo.setData(this.data);
			rinfo.save();
		}
		return maps.size() == 0;
	}

	private ResourceInfo getRes(RecInfo info) {
		String root = this.getRoot();
		String timeStr = info.year + "-" + info.month + "-" + info.day;
		ResourceInfo rinfo = ResourceManagement.instance.loadOrCreateTmpResource(root + "/" + timeStr,
				this.user.getUserName() + "/" + timeStr);
		return rinfo;
	}

	private String getRoot() {
		String root = toRecordString(this.user.getUserName()) + toRecordString(UserClient.getClientUsername());
		return root;
	}
}
