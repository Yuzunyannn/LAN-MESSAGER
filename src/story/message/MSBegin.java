package story.message;

import client.user.UserClient;
import core.Core;
import nbt.NBTBase;
import nbt.NBTTagCompound;
import network.Side;
import story.Story;
import user.User;

/** 服务端向客户端告知，需要创建story了！，客户端向服务端发送，告知创建成功！ */
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
		Story story = Story.newStory(nbt.getString("inst"), nbt.getString("sid"), serverNbt, Side.CLIENT);
		if (story != null)
			UserClient.sendToServer(new MSResult(nbt.getString("sid")));
	}

}
