package security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import log.Logger;

public class Key {
	/** 不对称加密算法 */
	public static final String KEY_ALGORITHM = "RSA";
	/** 公钥和私钥 */
	private final java.security.Key key;

	Key(java.security.Key key) {
		this.key = key;
	}

	public Key(byte[] keyData, boolean isPublic) throws InvalidKeySpecException {
		java.security.Key tmpKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			EncodedKeySpec keySpec;
			if (isPublic)
				keySpec = new X509EncodedKeySpec(keyData);
			else
				keySpec = new PKCS8EncodedKeySpec(keyData);
			if (isPublic)
				tmpKey = keyFactory.generatePublic(keySpec);
			else
				tmpKey = keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			Logger.log.error("钥类型初始化错误！", e);
		} catch (InvalidKeySpecException e) {
			throw e;
		} finally {
			this.key = tmpKey;
		}
	}

	public boolean isPublicKey() {
		return key instanceof PublicKey;
	}

	public boolean isPrivateKey() {
		return key instanceof PrivateKey;
	}

	public byte[] getKeyData() {
		return key.getEncoded();
	}

	/**
	 * 加解密
	 * 
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws IOException
	 */
	private byte[] crypt(byte[] data, int opmode, int maxBlock) throws NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		// 加解密类
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
		try {
			cipher.init(opmode, key);
		} catch (InvalidKeyException e) {
			Logger.log.error("加解密时，钥的类型出现错误！", e);
		}
		// 数据长度
		int dataSize = data.length;
		// 输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 位移量
		int offSet = 0;
		byte[] buf;
		while (true) {
			if (dataSize - offSet > maxBlock)
				buf = cipher.doFinal(data, offSet, maxBlock);
			else
				buf = cipher.doFinal(data, offSet, dataSize - offSet);
			offSet += maxBlock;
			out.write(buf);
			if (offSet >= dataSize)
				break;
		}
		buf = out.toByteArray();
		out.close();
		return buf;
	}

	/** 加密 */
	public byte[] encrypt(byte[] data) {
		try {
			return this.crypt(data, Cipher.ENCRYPT_MODE, 117);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
				| IOException e) {
			Logger.log.error("加密时候出现问题！", e);
		}
		return null;
	}

	/** 解密 */
	public byte[] decrypt(byte[] data) {
		try {
			return this.crypt(data, Cipher.DECRYPT_MODE, 128);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | IOException e) {
			Logger.log.error("解密时候出现问题！", e);
		} catch (BadPaddingException e) {
			Logger.log.warn("您的密钥和数据不匹配", e);
		}
		return null;
	}

}
