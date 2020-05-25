package com.fxz.channelswitcher.datatransferserver.auth.impl;


import com.fxz.channelswitcher.datatransferserver.auth.auth.IDigest;
import com.fxz.channelswitcher.datatransferserver.auth.auth.INonSymEncrypt;
import com.fxz.channelswitcher.datatransferserver.auth.auth.ISymEncrypt;
import com.fxz.channelswitcher.datatransferserver.auth.exceptions.EncryptExcepton;

/**
 * @ClassName: NonEncrypt 
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月2日 下午6:45:11  
 */
public class NonEncrypt implements IDigest, ISymEncrypt, INonSymEncrypt {

	@Override
	public void setKeys(String privatekey, String publickey) {

	}

	@Override
	public void init(int block) throws EncryptExcepton {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] encryptByPrivateKey(byte[] buffer, String privateKey) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return buffer;
	}

	@Override
	public byte[] encryptByPublicKey(byte[] buffer, String publicKey) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return buffer;
	}

	@Override
	public byte[] decryptByPrivateKey(byte[] buffer, String privateKey) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return buffer;
	}

	@Override
	public byte[] decryptByPublicKey(byte[] buffer, String publicKey) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return buffer;
	}

	@Override
	public String getPrivateKey() {
		// TODO Auto-generated method stub
		return "010203040506";
	}

	@Override
	public String getPublicKey() {
		// TODO Auto-generated method stub
		return "010203040506";
	}

	@Override
	public byte[] encrypt(byte[] buffer, String key) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return buffer;
	}

	@Override
	public byte[] decrypt(byte[] buffer, String key) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return buffer;
	}

	@Override
	public String digest(String mesg) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return "N/A";
	}

	@Override
	public String digest(byte[] buffer) throws EncryptExcepton {
		return "N/A";
	}

}
