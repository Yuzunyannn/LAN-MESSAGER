package transfer;

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

}
