package file;

import java.io.File;

import event.EventBusSynchronized;
import event.IEventBus;

public class FileSenderManager {

	/** 文件发送事件 */
	final public static IEventBus eventHandle = new EventBusSynchronized();

	static public File getFileWithKey(int key) {
		return new File("libs/mysql-connector-java-8.0.13.jar");
	}

}
