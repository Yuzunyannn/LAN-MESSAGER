package util;

import java.io.UnsupportedEncodingException;

import log.Logger;

public class Cast {

	static public int toInt(byte[] b) {
		return b[0] & 0xFF | (b[1] & 0xFF) << 8 | (b[2] & 0xFF) << 16 | (b[3] & 0xFF) << 24;
	}

	static public byte[] toBytes(int i) {
		return new byte[] { (byte) (i & 0xFF), (byte) ((i >> 8) & 0xFF), (byte) ((i >> 16) & 0xFF),
				(byte) ((i >> 24) & 0xFF) };
	}

	public static String toStringUTF8(byte[] bs) {
		try {
			return new String(bs, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger.log.warn("字符与byte转化错误！", e);
			return "";
		}
	}

	public static byte[] toBytesUTF8(String str) {
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger.log.warn("字符与byte转化错误！", e);
			return new byte[0];
		}
	}
}
