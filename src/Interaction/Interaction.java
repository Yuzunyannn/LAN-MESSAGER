package Interaction;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import story.Story;
import user.User;

public abstract class Interaction {
	/** 注册表 */
	static final private Map<String, Interaction> registers = new HashMap<String, Interaction>();

	/** 交互面板 */
	abstract public JPanel interactionPanel();

	/** 交互名称 */
	abstract public String name();

	/** @return 需要注册的story */
	abstract public Class<? extends Story> registerStory();

	/** 当交互被发起 */
	public void onLaunch(InteractionGroup group) {

	}

	/** 交互准备中时，用户加入 */
	public void onUserJoin(InteractionGroup group, User joiner) {

	}

	/** 交互准备中时，用户离开 */
	public void onUserLeave(InteractionGroup group, User joiner) {

	}

	/** 当交互开始 */
	public void onBegin(InteractionGroup group) {

	}

	/***/
}
