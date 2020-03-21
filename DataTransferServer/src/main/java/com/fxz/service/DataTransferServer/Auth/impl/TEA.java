package com.fxz.service.DataTransferServer.Auth.impl;

import com.fxz.service.DataTransferServer.Auth.auth.ISymEncrypt;
import com.fxz.service.DataTransferServer.Auth.auth.Utils;
import com.fxz.service.DataTransferServer.Auth.exceptions.EncryptExcepton;

public class TEA implements ISymEncrypt {

	public byte[] encrypt(byte[] buffer, String key) throws EncryptExcepton {
		// TODO Auto-generated method stub
		byte[] keybytes = Utils.Hex2Byte(key);
		if (keybytes.length < 16) {
			throw new EncryptExcepton("key length must be bigger than 16bytes");
		}
		try {
			int[] KEY = Utils.byteToInt(Utils.subBytes(keybytes, 0, 16), 0);
			int n = 8 - buffer.length % 8;
			byte[] encryptStr = new byte[buffer.length + n];
			encryptStr[0] = (byte) n;
			System.arraycopy(buffer, 0, encryptStr, n, buffer.length);
			byte[] result = new byte[encryptStr.length];
			for (int offset = 0; offset < result.length; offset += 8) {
				byte[] tempEncrpt = encrypt(encryptStr, offset, KEY, 32);
				System.arraycopy(tempEncrpt, 0, result, offset, 8);
			}
			return result;
		} catch (Exception e) {
			throw new EncryptExcepton(e);
		}
	}

	public byte[] decrypt(byte[] buffer, String key) throws EncryptExcepton {
		byte[] keybytes = Utils.Hex2Byte(key);
		if (keybytes.length < 16) {
			throw new EncryptExcepton("key length must be bigger than 16bytes");
		}
		try {
			int[] KEY = Utils.byteToInt(Utils.subBytes(keybytes, 0, 16), 0);
			byte[] decryptStr = null;
			byte[] tempDecrypt = new byte[buffer.length];
			for (int offset = 0; offset < buffer.length; offset += 8) {
				decryptStr = decrypt(buffer, offset, KEY, 32);
				System.arraycopy(decryptStr, 0, tempDecrypt, offset, 8);
			}
			int n = tempDecrypt[0];
			return Utils.subBytes(tempDecrypt, n, tempDecrypt.length - n);
		} catch (Exception e) {
			throw new EncryptExcepton(e);
		}
	}

	private byte[] encrypt(byte[] content, int offset, int[] key, int times) {// times为加密轮数
		int[] tempInt = Utils.byteToInt(content, offset);
		int y = tempInt[0], z = tempInt[1], sum = 0, i;
		int delta = 0x9e3779b9; // 这是算法标准给的值
		int a = key[0], b = key[1], c = key[2], d = key[3];
		for (i = 0; i < times; i++) {
			sum += delta;
			y += ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
			z += ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
		}
		tempInt[0] = y;
		tempInt[1] = z;
		return Utils.intToByte(tempInt, 0);
	}

	private byte[] decrypt(byte[] encryptContent, int offset, int[] key, int times) {
		int[] tempInt = Utils.byteToInt(encryptContent, offset);
		int y = tempInt[0], z = tempInt[1], sum = 0, i;
		int delta = 0x9e3779b9; // 这是算法标准给的值
		int a = key[0], b = key[1], c = key[2], d = key[3];
		if (times == 32) {
			sum = 0xC6EF3720; /* delta << 5 */
		} else if (times == 16) {
			sum = 0xE3779B90; /* delta << 4 */
		} else {
			sum = delta * times;
		}
		for (i = 0; i < times; i++) {
			z -= ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
			y -= ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
			sum -= delta;
		}
		tempInt[0] = y;
		tempInt[1] = z;

		return Utils.intToByte(tempInt, 0);
	}

	public static void main(String[] args) throws EncryptExcepton {
		String string = "abcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyzabcdefghigklmnopqrstuvwxyz";
		String passwd = "01234567890123456789000000000000";
		TEA tea = new TEA();
		String en_hex = Utils.Byte2Hex(tea.encrypt(string.getBytes(), passwd));
		System.out.println("en_str->" + en_hex);
		System.out.println("de_str->" + new String(tea.decrypt(Utils.Hex2Byte(en_hex), passwd)));
	}
}
