package file;

import event.Event;

public class EventFile extends Event {

	/** 接受文件的文件的stoty */
	protected final StoryFileSender story;

	public EventFile(boolean canCancel, boolean canStop, StoryFileSender story) {
		super(canCancel, canStop);
		this.story = story;
	}

	/** 获取进度 */
	public float getProgress() {
		return story.getProgress();
	}

	/** 获取文件名 */
	public String getFileName() {
		return story.fileName;
	}

	/** 获取文件大小 */
	public long getFileSize() {
		return story.fileSize;
	}
}
