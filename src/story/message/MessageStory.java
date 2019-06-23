package story.message;

import nbt.NBTTagCompound;
import user.User;
import user.message.MessageUser;

abstract public class MessageStory extends MessageUser implements Runnable {
	@Override
	protected void executeServer(User from, User to, NBTTagCompound nbt) {
	}


	@Override
	protected void executeClient(User from, NBTTagCompound nbt) {
	}
}
