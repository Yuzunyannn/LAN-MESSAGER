package story.message;

import core.Core;
import nbt.NBTBase;
import nbt.NBTTagCompound;
import network.Side;
import story.Story;
import user.User;

/** 服务端向客户端告知，需要创建story了！ */
public class MSBegin extends MessageStory {

	public MSBegin() {

	}

	public MSBegin(String storyInstanceId, String storyId, NBTTagCompound nbt) {
		this.nbt.setString("inst", storyInstanceId);
		this.nbt.setString("sid", storyId);
		if (nbt != null)
			this.nbt.setTag("upd", nbt);
	}

	@Override
	protected void executeClient(User from, NBTTagCompound nbt) {
		Core.task(this);
	}

	@Override
	public void run() {
		NBTTagCompound serverNbt = nbt.hasKey("upd", NBTBase.TAG_COMPOUND) ? nbt.getCompoundTag("upd") : null;
		Story.newStory(nbt.getString("inst"), nbt.getString("sid"), serverNbt, Side.CLIENT);
	}

}
