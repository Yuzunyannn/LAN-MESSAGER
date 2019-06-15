package story;

import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import client.user.UserClient;
import core.Core;
import log.Logger;
import nbt.NBTTagCompound;
import network.Side;
import story.message.MSBegin;
import story.message.MSEnd;
import story.message.MSMemberIn;
import story.message.MSNbtSend;
import user.User;

/** 描述一组人进行的任务，比如多人发送文件，多人进行小游戏等 */
public class Story implements ITickable {

	/** 注册的story */
	private static Map<String, Class<? extends Story>> storyMap = Collections
			.synchronizedMap(new TreeMap<String, Class<? extends Story>>());

	/** 注册一个story */
	public static void registerStory(String storyInstanceId, Class<? extends Story> storyClass) {
		if (storyInstanceId == null)
			throw new RuntimeException("注册是ID不能为null");
		if (storyClass == null)
			throw new RuntimeException("注册的类不能为null");
		if (storyMap.containsKey(storyInstanceId))
			throw new RuntimeException("重复的注册：" + storyInstanceId);
		storyMap.put(storyInstanceId, storyClass);
	}

	static private long nextId = 0;

	/** 随机获取一个没有的id */
	public static String giveStoryId() {
		Calendar cal = Calendar.getInstance();
		String id = nextId++ + "" + cal.get(Calendar.YEAR);
		return id;
	}

	/** 程序中所有story */
	static Map<String, Story> storys = new HashMap<String, Story>();

	/** 根据ID获取stroy */
	static public Story getStory(String storyId, Side side) {
		if (storys.containsKey(side.mark() + storyId))
			return storys.get(side.mark() + storyId);
		return null;
	}

	/** 移除一个story */
	static public Story removeStory(String storyId, Side side) {
		return storys.remove(side.mark() + storyId);
	}

	/** 添加story */
	static private void addStory(Story story) {
		storys.put(story.side.mark() + story.getId(), story);
	}

	/** 添加story成员 */
	static public void addMember(String storyId, Side side, User... users) {
		Story story = Story.getStory(storyId, side);
		if (story == null) {
			Logger.log.warn("storyId:" + storyId + "的story不存在");
			return;
		}
		story.addMember(users);
	}

	/** 创建一个story */
	static public Story newStory(String storyInstanceId, String storyId, NBTTagCompound nbt, Side side) {
		if (storyInstanceId == null || storyInstanceId.isEmpty()) {
			Logger.log.warn("传入的实例化id，为空！");
			return null;
		}
		if (Story.getStory(storyId, side) != null) {
			Logger.log.warn("sortyId：" + storyId + "，已经存在");
			return null;
		}
		Class<? extends Story> cls = storyMap.get(storyInstanceId);
		Story story = null;
		try {
			story = cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Logger.log.warn("传入的实例化id，实例化错误，是否有无参的构造函数呢？", e);
			return null;
		}
		story.side = side;
		story.instId = storyInstanceId;
		story.id = storyId;
		Story.addStory(story);
		if (nbt != null)
			story.updateFromServerPacket(nbt);
		Core.task(new StoryRun(story));
		return story;
	}

	private static class StoryRun implements Runnable {
		private final Story story;

		public StoryRun(Story story) {
			this.story = story;
		}

		@Override
		public void run() {
			this.story.onCreate();
			Core.task(this.story);
		}

	}

	/** 结束一个story */
	static public void endStory(String storyId, Side side) {
		Story story = Story.getStory(storyId, side);
		if (story == null)
			return;
		story.setEnd();
	}

	/** 添加一个接受信息 */
	static public void pushRev(String storyId, NBTTagCompound nbt, User user, Side side) {
		Story story = Story.getStory(storyId, side);
		if (story == null) {
			Logger.log.warn("storyId:" + storyId + "的story不存在，无法进行包录入");
			return;
		}
		synchronized (story.revs) {
			story.revs.addFirst(new AbstractMap.SimpleEntry<>(user, nbt));
		}
	}

	/** 表明当前组所处的位置，客户端，服务端 */
	private Side side;
	/** 组内的所有用户 */
	protected List<User> users = new LinkedList<User>();
	/** StoryId */
	private String id = "";
	/** InstanceId */
	private String instId = "";
	/** story是否结束 */
	private boolean isEnd = false;
	/** 所接受的消息数据 */
	private final LinkedList<Entry<User, NBTTagCompound>> revs = new LinkedList<Entry<User, NBTTagCompound>>();

	@Override
	final public int update() {
		if (this.isEnd) {
			this.onEnd();
			if (Story.removeStory(this.getId(), this.side) != this) {
				Logger.log.warn(this + "在移除story时候，出现移除内容于当前内容不相同的异常！！！");
			}
			if (this.isServer()) {
				// 告诉所有人，结束了！
				for (User user : users) {
					user.sendMesage(new MSEnd(this.getId()));
				}
			}
			return ITickable.END;
		}
		while (true) {
			Entry<User, NBTTagCompound> entry;
			synchronized (revs) {
				if (revs.isEmpty())
					break;
				entry = revs.getLast();
				revs.removeLast();
			}
			if (entry.getValue() == null) {
				this.setEnd();
				return ITickable.SUCCESS;
			}
			this.onGet(entry.getValue(), entry.getKey());
		}
		this.onTick();
		return ITickable.SUCCESS;
	}

	/** 设置结束 */
	protected void setEnd() {
		isEnd = true;
	}

	/** 是否结束 */
	public boolean isEnd() {
		return this.isEnd;
	}

	/** 获取story唯一编号 */
	public String getId() {
		return id;
	}

	/** 获取注册id */
	public String getInstanceId() {
		return instId;
	}

	public boolean isClient() {
		return side.isClient();
	}

	public boolean isServer() {
		return side.isServer();
	}

	@Override
	public String toString() {
		return '[' + (this.isServer() ? "Server" : "Client") + ']' + "[stroy(" + this.getId() + ")]";
	}

	public boolean hasMember(User user) {
		for (User u : users) {
			if (u.equals(user))
				return true;
		}
		return false;
	}

	/** 添加用户 */
	public void addMember(User... users) {
		if (this.isEnd) {
			Logger.log.warn(this + "无法向结束后的story添加新成员");
			return;
		}
		if (users == null || users.length == 0)
			return;
		if (this.isClient()) {
			for (User u : users) {
				if (this.hasMember(u))
					continue;
				if (this.onMemberJoin(u)) {
					this.users.add(u);
				}
			}
			return;
		}

		List<User> originUser = new LinkedList<User>();
		List<User> newUser = new LinkedList<User>();
		for (User u : this.users)
			originUser.add(u);

		for (User u : users) {
			if (this.hasMember(u))
				continue;
			if (this.onMemberJoin(u)) {
				this.users.add(u);
				newUser.add(u);
			}
		}
		// 给原始user发送新添加的用户
		for (User u : originUser)
			u.sendMesage(new MSMemberIn(this.getId(), users));
		// 给新添加的用户发送更新
		for (User u : newUser) {
			u.sendMesage(new MSBegin(this.getInstanceId(), this.getId(), this.packetToClientUpdate()));
			u.sendMesage(new MSMemberIn(this.getId(), this.users));
		}
	}

	/** 移除用户 */
	public void removeMember(User user) {
		Iterator<User> itr = users.iterator();
		while (itr.hasNext()) {
			User u = itr.next();
			if (u.equals(user)) {
				this.onMemberLeave(u);
				itr.remove();
				u.sendMesage(new MSEnd(this.getId()));
				return;
			}
		}
	}

	/** 对有所用户发送一个数据 */
	public void sendData(NBTTagCompound nbt) {
		if (nbt == null)
			return;
		if (this.isServer()) {
			for (User user : users)
				user.sendMesage(new MSNbtSend(this.getId(), nbt));
		} else {
			UserClient.sendToServer(new MSNbtSend(this.getId(), nbt));
		}
	}

	/** 对指定用户发送一个数据 */
	public void sendData(NBTTagCompound nbt, User user) {
		if (nbt == null || user == null)
			return;
		if (this.isServer()) {
			user.sendMesage(new MSNbtSend(this.getId(), nbt));
		}
	}

	/** 给客户端首次创建story的时候，发送的数据 */
	public NBTTagCompound packetToClientUpdate() {
		return null;
	}

	/** 收到更新内容开始恢复 */
	public void updateFromServerPacket(NBTTagCompound nbt) {

	}

	/**
	 * 当有新用户加入，
	 * 
	 * @return 返回false，拒绝加入
	 */
	protected boolean onMemberJoin(User user) {
		return true;
	}

	/**
	 * 当有新用户离开
	 */
	protected void onMemberLeave(User user) {
	}

	/**
	 * 收到发送的数据，处理这个数据
	 * 
	 * @from 从某个用户发来的，只有服务端有效，否则为User.Empty
	 */
	protected void onGet(NBTTagCompound nbt, User from) {

	}

	/** 被创建 */
	protected void onCreate() {

	}

	/** 当死亡 */
	protected void onEnd() {

	}

	/** 每tick更新 */
	protected void onTick() {

	}

}
