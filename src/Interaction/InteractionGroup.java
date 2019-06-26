package Interaction;

public class InteractionGroup {

	/** 描述group的标号 */
	final String id;
	/** 是否已经开始了互动 */
	boolean isRunning = false;

	public InteractionGroup(String id) {
		this.id = id;
	}

	/** 互动开始 */
	public void setBegin() {
		this.isRunning = true;
	}

	/** 互动是否正在运行 */
	public boolean interacting() {
		return this.isRunning;
	}

}
