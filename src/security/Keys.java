package security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import log.Logger;

public class Keys {

	private Key keyPublic;
	private Key keyPrivate;

	public Keys() {
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance(Key.KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			Logger.log.error("创建钥出现问题！", e);
			return;
		}
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		keyPublic = new Key(publicKey);
		keyPrivate = new Key(privateKey);
	}

	public Key getPublicKey() {
		return keyPublic;
	}

	public Key getPrivateKey() {
		return keyPrivate;
	}
}
