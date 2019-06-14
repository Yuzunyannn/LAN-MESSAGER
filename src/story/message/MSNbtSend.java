package story.message;

import nbt.NBTTagCompound;
import network.Side;
import story.Story;
import user.User;
import user.message.MessageUser;

public class MSNbtSend extends MessageUser {
	public MSNbtSend() {

	}

	public MSNbtSend(String storyId, NBTTagCompound nbt) {
		this.nbt.setString("sid", storyId);
		this.nbt.setTag("nbt", nbt);
	}

	@Override
	protected void executeClient(User from, NBTTagCompound nbt) {
		Story.pushRev(nbt.getString("sid"), nbt, User.EMPTY, Side.CLIENT);
	}

	@Override
	protected void executeServer(User from, User to, NBTTagCompound nbt) {
		Story.pushRev(nbt.getString("sid"), nbt, from, Side.SERVER);
	}

}
