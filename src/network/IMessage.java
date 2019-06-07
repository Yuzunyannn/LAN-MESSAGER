package network;

import java.io.IOException;
import java.nio.ByteBuffer;

/** 发送使用的消息 */
public interface IMessage {
	/** 通过bytes还原数据 */
	public void fromBytes(ByteBuffer buf) throws IOException;

	/** 将数据写入bytes */
	public void toBytes(ByteBuffer buf) throws IOException;

	/** 执行 */
	public void execute(Connection con);

}