package com.fxz.service.DataTransferServer.Auth.impl;

import java.util.Random;

import com.fxz.service.DataTransferServer.Auth.auth.Utils;
import com.fxz.service.DataTransferServer.Auth.exceptions.EncryptExcepton;

public class RC4 {
	public byte[] convertData(byte[] buffer, String key) {
		String lkey = "0123456789abcdefg";
		if (key != null) {
			lkey = key + lkey;
		}
		if (buffer == null) {
			return buffer;
		}
		return RC4Base(buffer, lkey);
	}

	private byte[] RC4Base(byte[] input, String mKkey) {
		int x = 0;
		int y = 0;
		byte key[] = initKey(mKkey);
		int xorIndex;
		byte[] result = new byte[input.length];
		for (int i = 0; i < input.length; i++) {
			x = (x + 1) & 0xff;
			y = ((key[x] & 0xff) + y) & 0xff;
			byte tmp = key[x];
			key[x] = key[y];
			key[y] = tmp;
			xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
			result[i] = (byte) (input[i] ^ key[xorIndex]);
		}
		return result;
	}

	private byte[] initKey(String aKey) {
		byte[] b_key = aKey.getBytes();
		byte state[] = new byte[256];

		for (int i = 0; i < 256; i++) {
			state[i] = (byte) i;
		}
		int index1 = 0;
		int index2 = 0;
		if (b_key == null || b_key.length == 0) {
			return null;
		}
		for (int i = 0; i < 256; i++) {
			index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
			byte tmp = state[i];
			state[i] = state[index2];
			state[index2] = tmp;
			index1 = (index1 + 1) % b_key.length;
		}
		return state;
	}

	public static void main(String[] args) throws EncryptExcepton {
		// TODO Auto-generated method stub
		byte[] buffer = new byte[1024 * 1024 ];
		new Random().nextBytes(buffer);
		RC4 rc4 = new RC4();
		String pass = null;
		System.out.println("OrigHash->" + Utils.digest(buffer));
		byte[] convert = rc4.convertData(buffer, pass);
		System.out.println("ConvertHash->" + Utils.digest(convert));
		System.out.println("RecHash->" + Utils.digest(rc4.convertData(convert, pass)));
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			 rc4.convertData(buffer, pass);
		}
		System.out.println("TimeElapsed->" + (System.currentTimeMillis() - start) + "  ms");

	}

}
