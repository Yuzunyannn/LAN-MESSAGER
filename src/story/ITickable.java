package story;

public interface ITickable {

	/** 更新成功 */
	static final int SUCCESS = 0;
	/** 更新失败 */
	static final int FAIL = 1;
	/** 更新结束，将移除tick更新队列 */
	static final int END = 1;

	/**
	 * 每一个tick的更新
	 * 
	 * @return 更新的状态标识符
	 */
	int update();
}
