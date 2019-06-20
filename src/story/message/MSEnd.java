package story.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import network.Connection;
import network.IMessage;
import network.Side;
import story.Story;
import user.User;
import util.StreamHelper;

/** 结束一个story，只能作用于服务端发给客户端 */
public class MSEnd implements IMessage {
	String storyId = "";

	public MSEnd() {

	}

	public MSEnd(String storyId) {
		this.storyId = storyId;
	}

	@Override
	public void fromBytes(ByteBuffer buf) throws IOException {
		this.storyId = StreamHelper.readString(buf);
	}

	@Override
	public void toBytes(ByteBuffer buf) throws IOException {
		StreamHelper.writeString(buf, storyId);
	}

	@Override
	public void execute(Connection con) {
		if (con.isServer())
			return;
		Story.pushRev(storyId, null, User.EMPTY, Side.CLIENT);
	}

}
