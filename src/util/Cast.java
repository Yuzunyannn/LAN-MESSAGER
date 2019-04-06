package util;

public class Cast {

	static public int toInt(byte[] b) {
		return b[0] & 0xFF | (b[1] & 0xFF) << 8 | (b[2] & 0xFF) << 16 | (b[3] & 0xFF) << 24;
	}

	static public byte[] toBytes(int i) {
		return new byte[] { (byte) (i & 0xFF), (byte) ((i >> 8) & 0xFF), (byte) ((i >> 16) & 0xFF),
				(byte) ((i >> 24) & 0xFF) };
	}
}
