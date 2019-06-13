package story;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import client.user.UserClient;
import log.Logger;
import nbt.NBTTagCompound;
import network.Side;
import user.UOnline;
import user.User;

/** 描述一组人进行的任务，比如多人发送文件，多人进行小游戏等 */
public class Story implements ITickable {

	/** 注册的story */
	public static List<Class<? extends Story>> storyMap = Collections
			.synchronizedList(new ArrayList<Class<? extends Story>>());

	/** 程序中所有story */
	static Map<String, Story> storys = new HashMap<String, Story>();

	/** 创建一个story */
	static public void newStory(int storyInstanceId, String storyId, Side side, List<User> users) {
		if (storyInstanceId < 0 || storyInstanceId >= storyMap.size()) {
			Logger.log.warn("传入的实例化id，越界！");
			return;
		}
		if (storys.containsKey(storyId)) {
			Logger.log.warn("sortyId：" + storyId + "，已经存在");
			return;
		}
		Class<? extends Story> cls = storyMap.get(storyInstanceId);
		Story story = null;
		try {
			story = cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Logger.log.warn("传入的实例化id，实例化错误，是否有无参的构造函数呢？", e);
			return;
		}
		story.side = side;
		story.id = storyId;
		story.users = users;
		storys.put(storyId, story);
	}

	/** 创建一个story */
	static public void newStory(int storyInstanceId, String storyId, Side side, String... users) {
		List<User> us = new LinkedList<User>();
		// 这里之所以没有全用UOnline.getInstance().getUser是因为在debug中，UOnline只有一个Server的
		for (String str : users)
			if (side == Side.SERVER)
				us.add(UOnline.getInstance().getUser(str));
			else
				us.add(new UserClient(str));
		Story.newStory(storyInstanceId, storyId, side, us);
	}

	/** 根据ID获取stroy */
	static public Story getStory(String storyId) {
		if (storys.containsKey(storyId))
			return storys.get(storyId);
		return null;
	}

	/** 添加一个接受信息 */
	static void pushRev(String storyId, NBTTagCompound nbt) {
		Story story = Story.getStory(storyId);
		if (story == null) {
			Logger.log.warn("storyId:" + storyId + "的story不存在");
		}
		synchronized (story.revs) {
			story.revs.addFirst(nbt);
		}
	}

	/** 表明当前组所处的位置，客户端，服务端 */
	private Side side;
	/** 组内的所有用户 */
	protected List<User> users = new LinkedList<User>();
	/** StoryId */
	private String id = "";
	/** story是否结束 */
	private boolean isEnd = false;
	/** 所接受的消息数据 */
	private LinkedList<NBTTagCompound> revs = new LinkedList<NBTTagCompound>();

	@Override
	final public int update() {
		while (!revs.isEmpty()) {
			NBTTagCompound nbt;
			synchronized (revs) {
				nbt = revs.getLast();
				revs.removeLast();
			}
			this.onGet(nbt);
		}
		this.onTick();
		return isEnd ? ITickable.END : ITickable.SUCCESS;
	}

	/** 设置结束 */
	public void setEnd() {
		isEnd = true;
	}

	/** 获取story唯一编号 */
	public String getId() {
		return id;
	}

	public boolean isClient() {
		return side.isClient();
	}

	public boolean isServer() {
		return side.isServer();
	}

	/** 对有所用户发送一个数据 */
	public void sendData(NBTTagCompound nbt) {

	}

	/** 收到发送的数据，处理这个数据 */
	protected void onGet(NBTTagCompound nbt) {

	}

	/** 每tick更新 */
	protected void onTick() {

	}

}
