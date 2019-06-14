package story.message;

import core.Core;
import nbt.NBTTagCompound;
import network.Side;
import story.Story;
import user.User;

/** 服务端向客户端告知，需要创建story了！ */
public class MSBegin extends MessageStory {

	public MSBegin() {
		
	}
	
	public MSBegin(String storyInstanceId, String storyId) {
		this.nbt.setString("inst", storyInstanceId);
		this.nbt.setString("sid", storyId);
	}

	@Override
	protected void executeClient(User from, NBTTagCompound nbt) {
		Core.task(this);
	}

	@Override
	public void run() {
		Story.newStory(nbt.getString("inst"), nbt.getString("sid"), Side.CLIENT);
	}

}
