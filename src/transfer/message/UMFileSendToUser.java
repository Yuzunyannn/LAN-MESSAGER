package transfer.message;

import core.Core;
import nbt.NBTTagCompound;
import network.Side;
import story.Story;
import transfer.StoryFileSender;
import user.User;
import user.message.MessageUser;

public class UMFileSendToUser extends MessageUser {

	public UMFileSendToUser() {

	}

	public UMFileSendToUser(User toUser, int key, String fileName) {
		super(toUser);
		this.nbt.setInteger("key", key);
		this.nbt.setString("name", fileName);
	}

	@Override
	protected void executeClient(User from, NBTTagCompound nbt) {

	}

	@Override
	protected void executeServer(User from, User to, NBTTagCompound nbt) {
		Core.task(new Runnable() {
			@Override
			public void run() {
				StoryFileSender story = (StoryFileSender) Story.newStory("fileSender", Story.giveStoryId(), null,
						Side.SERVER);
				StoryFileSender.initStory(story, from, nbt.getInteger("key"));
				story.addMember(to);
			}
		});

	}

}
