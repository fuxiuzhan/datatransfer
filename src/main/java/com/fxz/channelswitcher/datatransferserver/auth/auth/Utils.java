package com.fxz.channelswitcher.datatransferserver.auth.auth;

import com.fxz.channelswitcher.datatransferserver.auth.exceptions.EncryptExcepton;
import net.sf.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Random;

/**
 * @ClassName: Utils
 * @Description: TODO
 * @author: Administrator
 * @date: 2018年1月25日 下午2:12:50
 */
public class Utils {
	public static byte[] Hex2Byte(String hexstr) throws EncryptExcepton {
		if (hexstr.length() < 1) {
			throw new EncryptExcepton("input hexstr must be not null");
		}
		if (hexstr.length() % 2 != 0) {
			throw new EncryptExcepton("hexstr length%2!=0");
		}
		String regex = "^[A-Fa-f0-9]+$";
		if (!hexstr.matches(regex)) {
			throw new EncryptExcepton("hexstr format error!");
		}
		byte[] result = new byte[hexstr.length() / 2];
		for (int i = 0; i < hexstr.length() / 2; i++) {
			int high = Integer.parseInt(hexstr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexstr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static String Byte2Hex(byte[] buffer) throws EncryptExcepton {
		if (buffer == null || buffer.length == 0) {
			throw new EncryptExcepton("input buffer is null");
		}
		StringBuffer sBuffer = new StringBuffer(buffer.length);
		String sTemp;
		for (int i = 0; i < buffer.length; i++) {
			sTemp = Integer.toHexString(0xFF & buffer[i]);
			if (sTemp.length() < 2) {
				sBuffer.append(0);
			}
			sBuffer.append(sTemp.toUpperCase());
		}
		return sBuffer.toString();
	}

	public static String getRamdomMesg(int length) {
		String basestring = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int pos = random.nextInt(basestring.length());
			sBuilder.append(basestring.substring(pos, pos + 1));
		}
		return sBuilder.toString();
	}

	public static String getString(byte[] buffer) {
		return new String(buffer, 0, buffer.length);
	}

	public static byte[] subBytes(byte[] buffer, int pos, int length) {
		byte[] temp = new byte[length];
		System.arraycopy(buffer, pos, temp, 0, length);
		return temp;
	}

	public static byte[] bytesComb(byte[] buffer1, byte[] buffer2) {
		if (buffer1 == null) {
			return buffer2;
		}
		if (buffer2 == null) {
			return buffer1;
		}
		byte[] temp = new byte[buffer1.length + buffer2.length];
		System.arraycopy(buffer1, 0, temp, 0, buffer1.length);
		System.arraycopy(buffer2, 0, temp, buffer1.length, buffer2.length);
		return temp;
	}

	public static byte[] Int2Byte(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	public static byte[] Long2Byte(long number) {
		long temp = number;
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(temp & 0xff).byteValue();
			temp = temp >> 8;
		}
		return b;
	}

	public static String Long2Hex(long num) throws EncryptExcepton {
		return Byte2Hex(Long2Byte(num));
	}

	public static long Hex2Long(String hex) throws EncryptExcepton {
		return Byte2Long(Hex2Byte(hex));
	}

	public static long Byte2Long(byte[] b) {
		long s = 0;
		long s0 = b[0] & 0xff;// 最低位
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		long s4 = b[4] & 0xff;// 最低位
		long s5 = b[5] & 0xff;
		long s6 = b[6] & 0xff;
		long s7 = b[7] & 0xff;
		// s0不变
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}

	public static byte[] intToByte(int[] content, int offset) {
		byte[] result = new byte[content.length << 2];
		for (int i = 0, j = offset; j < result.length; i++, j += 4) {
			result[j + 3] = (byte) (content[i] & 0xff);
			result[j + 2] = (byte) ((content[i] >> 8) & 0xff);
			result[j + 1] = (byte) ((content[i] >> 16) & 0xff);
			result[j] = (byte) ((content[i] >> 24) & 0xff);
		}
		return result;
	}

	public static int[] byteToInt(byte[] content, int offset) {
		int[] result = new int[content.length >> 2];
		for (int i = 0, j = offset; j < content.length; i++, j += 4) {
			result[i] = transform(content[j + 3]) | transform(content[j + 2]) << 8 | transform(content[j + 1]) << 16 | content[j] << 24;
		}
		return result;
	}

	private static int transform(byte temp) {
		int tempInt = temp;
		if (tempInt < 0) {
			tempInt += 256;
		}
		return tempInt;
	}

	public static String object2Json(Object obj) {
		return JSONObject.fromObject(obj).toString();
	}

	public static Object json2Object(String josnstr, @SuppressWarnings("rawtypes") Class clazz, @SuppressWarnings("rawtypes") HashMap<String, Class> classMap) {
		if (classMap == null) {
			return JSONObject.toBean(JSONObject.fromObject(josnstr), clazz);
		} else {
			return JSONObject.toBean(JSONObject.fromObject(josnstr), clazz, classMap);
		}
	}

	public static String digest(String mesg) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return digest(mesg.getBytes());
	}

	public static String digest(byte[] buffer) throws EncryptExcepton {
		// TODO Auto-generated method stub
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			return Utils.Byte2Hex(md5.digest(buffer));
		} catch (Exception e) {
			throw new EncryptExcepton(e);
		}
	}
}
