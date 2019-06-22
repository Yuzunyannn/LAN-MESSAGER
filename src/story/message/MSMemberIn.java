package story.message;

import java.util.List;

import client.user.UserClient;
import core.Core;
import nbt.NBTBase;
import nbt.NBTTagCompound;
import nbt.NBTTagList;
import network.Side;
import story.Story;
import user.User;

/** 服务端向客户端告知，添加新的成员进入 */
public class MSMemberIn extends MessageStory {

	public MSMemberIn() {

	}

	public MSMemberIn(String storyId, User... users) {
		NBTTagList list = new NBTTagList();
		for (User user : users)
			list.appendTag(user.toString());
		this.nbt.setTag("ulist", list);
		this.nbt.setString("sid", storyId);
	}

	public MSMemberIn(String storyId, List<User> users) {
		NBTTagList list = new NBTTagList();
		for (User user : users)
			list.appendTag(user.toString());
		this.nbt.setTag("ulist", list);
		this.nbt.setString("sid", storyId);
	}

	@Override
	public void run() {
		NBTTagList list = (NBTTagList) nbt.getTag("ulist");
		User[] arr = new User[list.size()];
		int i = 0;
		for (NBTBase nstr : list) {
			User user = new UserClient(nstr.toString());
			arr[i++] = user;
		}
		Story.addMember(this.nbt.getString("sid"), Side.CLIENT, arr);
	}

	@Override
	protected void executeClient(User from, NBTTagCompound nbt) {
		Core.task(this);
	}

}
