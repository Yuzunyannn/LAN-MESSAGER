package file;

/** 发送文件事件 */
public class EventFileSend extends EventFile {

	public EventFileSend(StoryFileSender story) {
		super(false, false, story);
	}

	public static class Start extends EventFileSend {
		public Start(StoryFileSender story) {
			super(story);
		}
	}

	public static class Finish extends EventFileSend {
		public Finish(StoryFileSender story) {
			super(story);
		}
	}

}
