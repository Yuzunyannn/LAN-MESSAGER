package story.message;

import core.Core;
import nbt.NBTTagCompound;
import network.Side;
import story.Story;
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
		Story.newStory("debug", "dd", Side.SERVER, from);
	}

}
