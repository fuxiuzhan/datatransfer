package com.fxz.channelswitcher.datatransferserver.auth.impl;

import com.fxz.channelswitcher.datatransferserver.auth.auth.Utils;
import com.fxz.channelswitcher.datatransferserver.auth.exceptions.EncryptExcepton;

import java.util.Arrays;
import java.util.Random;


public class Convert {
	private byte[] keyarry = null;

	@Override
	public String toString() {
		return "Convert [keyarry=" + Arrays.toString(keyarry) + "]";
	}

	public byte[] convert(byte[] buffer) {
		if (buffer == null) {
			return buffer;
		}
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = (byte) (buffer[i] ^ keyarry[i % keyarry.length] ^ 0xFF);
		}
		return buffer;
	}

	public void initKey(String key) {
		String rkey = "abcdefghijklmnopq1234567890";
		if (key != null) {
			rkey = key + rkey;
		}
		keyarry = new byte[256];
		int index = 0;
		byte[] keytmp = rkey.getBytes();
		for (int i = 0; i < keyarry.length; i++) {
			if (i % 2 == 0) {
				keyarry[i] = keytmp[index++ % keytmp.length];
			} else {
				keyarry[i] = (byte) (keytmp[index++ % keytmp.length] ^ 0xFF);
			}
		}
	}

	public static void main(String[] args) throws EncryptExcepton {
		byte[] buffer = new byte[1024*1024];//"0123456789abcdefghijklmnopqrstuvwxyz".getBytes();
		 new Random().nextBytes(buffer);
		System.out.println("Hash1->" + Utils.digest(buffer));
		Convert convert = new Convert();
		convert.initKey("+-=+-=+-=");
		byte[] after = convert.convert(buffer);
		//System.out.println(Arrays.toString(after));
		System.out.println("Hash2->" + Utils.digest(after));
		byte[] org = convert.convert(after);
		System.out.println("Hash3->" + Utils.digest(org));
		//System.out.println(Arrays.toString(org));

		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			convert.convert(after);
		}
		System.out.println("TimeElapsed->" + (System.currentTimeMillis() - start) + "  ms");
	}
}
