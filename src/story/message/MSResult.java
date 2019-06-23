package story.message;

import core.Core;
import log.Logger;
import nbt.NBTTagCompound;
import network.Side;
import story.Story;
import user.User;

public class MSResult extends MessageStory {

	public MSResult() {

	}

	public MSResult(String storyId) {
		this.nbt.setString("sid", storyId);
	}

	User from;

	@Override
	protected void executeServer(User from, User to, NBTTagCompound nbt) {
		this.from = from;
		Core.task(this);
	}

	@Override
	public void run() {
		String storyId = nbt.getString("sid");
		Story story = Story.getStory(storyId, Side.SERVER);
		if (story == null) {
			Logger.log.warn("客户" + from + "创建了一个不存在的story:" + storyId);
			return;
		}
		story.clientStoryCreateSuccess(from);
	}

}
