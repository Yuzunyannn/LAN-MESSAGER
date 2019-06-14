package util;

import java.io.IOException;
import java.nio.ByteBuffer;

public class StreamHelper {

	public static void writeBytes(ByteBuffer buf, byte[] datas) throws IOException {
		if (datas.length > 0xffff - 1)
			throw new IOException("bytes数据量过大");
		buf.putShort((short) datas.length);
		buf.put(datas);
	}

	public static byte[] readBytes(ByteBuffer buf) {
		short size = buf.getShort();
		byte[] datas = new byte[size];
		buf.get(datas);
		return datas;
	}

	public static void writeString(ByteBuffer buf, String str) throws IOException {
		StreamHelper.writeBytes(buf, str.getBytes("UTF-8"));
	}

	public static String readString(ByteBuffer buf) throws IOException {
		return new String(StreamHelper.readBytes(buf), "UTF-8");
	}
}
