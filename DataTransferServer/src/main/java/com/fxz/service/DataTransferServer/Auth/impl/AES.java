package com.fxz.service.DataTransferServer.Auth.impl;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.fxz.service.DataTransferServer.Auth.auth.ISymEncrypt;
import com.fxz.service.DataTransferServer.Auth.auth.Utils;
import com.fxz.service.DataTransferServer.Auth.exceptions.EncryptExcepton;

public class AES implements ISymEncrypt {

	public synchronized byte[] encrypt(byte[] buffer, String key) throws EncryptExcepton {
		// TODO Auto-generated method stub
		if (buffer == null || buffer.length == 0) {
			throw new EncryptExcepton("buffer is null!");
		}
		try {
			key = key + "0123456789abcdefghijklmn";
			SecureRandom ramdom = SecureRandom.getInstance("SHA1PRNG");
			ramdom.setSeed(Utils.subBytes(key.getBytes(), 0, 16));
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128, ramdom);
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] encodeforamt = secretKey.getEncoded();
			SecretKeySpec keysep = new SecretKeySpec(encodeforamt, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keysep);
			return cipher.doFinal(buffer);
		} catch (Exception e) {
			throw new EncryptExcepton(e);
		}
	}

	public synchronized byte[] decrypt(byte[] buffer, String key) throws EncryptExcepton {
		// TODO Auto-generated method stub
		if (buffer == null || buffer.length == 0) {
			throw new EncryptExcepton("buffer is null!");
		}
		try {
			key = key + "0123456789abcdefghijklmn";
			SecureRandom ramdom = SecureRandom.getInstance("SHA1PRNG");
			ramdom.setSeed(Utils.subBytes(key.getBytes(), 0, 16));
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128, ramdom);
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] encodeforamt = secretKey.getEncoded();
			SecretKeySpec keysep = new SecretKeySpec(encodeforamt, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, keysep);
			return cipher.doFinal(buffer);
		} catch (Exception e) {
			throw new EncryptExcepton(e);
		}
	}

}
