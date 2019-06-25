package transfer;

import user.User;

/** 接受文件事件 */
public class EventFileRecv extends EventFile {

	public EventFileRecv(StoryFileSender story) {
		super(false, false, story);
	}

	public static class Start extends EventFileRecv {
		public Start(StoryFileSender story) {
			super(story);
		}
	}

	public static class Finish extends EventFileRecv {
		public Finish(StoryFileSender story) {
			super(story);
		}
	}

	//获取发送方
	public User getFrom() {
		return this.story.daddy;
	}
}
