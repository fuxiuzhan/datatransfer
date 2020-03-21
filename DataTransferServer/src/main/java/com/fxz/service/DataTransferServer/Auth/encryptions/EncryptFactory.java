package com.fxz.service.DataTransferServer.Auth.encryptions;

import java.util.HashMap;

import com.fxz.service.DataTransferServer.Auth.auth.IDigest;
import com.fxz.service.DataTransferServer.Auth.auth.INonSymEncrypt;
import com.fxz.service.DataTransferServer.Auth.auth.ISymEncrypt;
import com.fxz.service.DataTransferServer.Auth.impl.AES;
import com.fxz.service.DataTransferServer.Auth.impl.CRC_32;
import com.fxz.service.DataTransferServer.Auth.impl.DES;
import com.fxz.service.DataTransferServer.Auth.impl.MD5;
import com.fxz.service.DataTransferServer.Auth.impl.NonEncrypt;
import com.fxz.service.DataTransferServer.Auth.impl.RSA;
import com.fxz.service.DataTransferServer.Auth.impl.TEA;

/**
 * @ClassName: EncryptFactory
 * @Description: TODO
 * @author: Administrator
 * @date: 2018年1月25日 下午2:37:25
 */
public class EncryptFactory {
	private static HashMap<String, IDigest> digestMap = new HashMap<String, IDigest>();
	private static HashMap<String, INonSymEncrypt> nonsymMap = new HashMap<String, INonSymEncrypt>();
	private static HashMap<String, ISymEncrypt> symMap = new HashMap<String, ISymEncrypt>();
	static {
		// fill encrypt factory
		nonsymMap.put("none", new NonEncrypt());
		nonsymMap.put("rsa", new RSA());
		//nonsymMap.put("ecc", new ECC());
		symMap.put("aes", new AES());
		symMap.put("des", new DES());
		symMap.put("tea", new TEA());
		symMap.put("none", new NonEncrypt());
		digestMap.put("md5", new MD5());
		digestMap.put("crc32", new CRC_32());
		digestMap.put("none", new NonEncrypt());
	}

	public static void putSymEncrypt(String name, ISymEncrypt sym) {
		symMap.put(name, sym);
	}

	public static void putDigest(String name, IDigest digest) {
		digestMap.put(name, digest);
	}

	public static void putNonSymEncrypt(String name, INonSymEncrypt nonsym) {
		nonsymMap.put(name, nonsym);
	}

	public static IDigest getDigest(String digest) {
		return digestMap.get(digest);
	}

	public static INonSymEncrypt getNonSymEncrypt(String nonsym) {
		return nonsymMap.get(nonsym);
	}

	public static ISymEncrypt getSymEncrypt(String sym) {
		return symMap.get(sym);
	}
}
