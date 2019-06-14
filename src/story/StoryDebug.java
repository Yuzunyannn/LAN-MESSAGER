package story;

import log.Logger;

public class StoryDebug extends Story {
	int i = 0;

	@Override
	protected void onCreate() {
		String str = this.isClient() ? "[client]" : "[server]";
		Logger.log.impart(str + "story创建成功了！");
	}

	@Override
	protected void onEnd() {
		String str = this.isClient() ? "[client]" : "[server]";
		Logger.log.impart(str + "story结束了！");
	}

	@Override
	protected void onTick() {
		super.onTick();
		i++;
		if (i > 200) {
			this.setEnd();
		}
		if (i % 20 == 0) {
			String str = this.isClient() ? "[client]" : "[server]";
			Logger.log.impart(str + i);
		}
	}
}
