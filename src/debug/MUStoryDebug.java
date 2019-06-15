package debug;

import core.Core;
import file.StoryFileSender;
import nbt.NBTTagCompound;
import network.Side;
import story.Story;
import user.UOnline;
import user.User;
import user.message.MessageUser;

public class MUStoryDebug extends MessageUser implements Runnable {
	User from;

	@Override
	protected void executeClient(User from, NBTTagCompound nbt) {

	}

	@Override
	protected void executeServer(User from, User to, NBTTagCompound nbt) {
		this.from = from;
		Core.task(this);
	}

	@Override
	public void run() {
		StoryFileSender story = (StoryFileSender) Story.newStory("fileSender", Story.giveStoryId(), null, Side.SERVER);
		StoryFileSender.initStory(story, this.from, 0);
		story.addMember(from, UOnline.getInstance().getUser("ssj"));
	}

}
