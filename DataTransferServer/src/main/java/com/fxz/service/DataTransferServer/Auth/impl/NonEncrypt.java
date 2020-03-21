package com.fxz.service.DataTransferServer.Auth.impl;

import com.fxz.service.DataTransferServer.Auth.auth.IDigest;
import com.fxz.service.DataTransferServer.Auth.auth.INonSymEncrypt;
import com.fxz.service.DataTransferServer.Auth.auth.ISymEncrypt;
import com.fxz.service.DataTransferServer.Auth.exceptions.EncryptExcepton;

/** 
 * @ClassName: NonEncrypt 
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月2日 下午6:45:11  
 */
public class NonEncrypt implements IDigest, ISymEncrypt, INonSymEncrypt {

	public void setKeys(String privatekey, String publickey) {

	}

	public void init(int block) throws EncryptExcepton {
		// TODO Auto-generated method stub

	}

	public byte[] encryptByPrivateKey(byte[] buffer, String privateKey) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return buffer;
	}

	public byte[] encryptByPublicKey(byte[] buffer, String publicKey) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return buffer;
	}

	public byte[] decryptByPrivateKey(byte[] buffer, String privateKey) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return buffer;
	}

	public byte[] decryptByPublicKey(byte[] buffer, String publicKey) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return buffer;
	}

	public String getPrivateKey() {
		// TODO Auto-generated method stub
		return "010203040506";
	}

	public String getPublicKey() {
		// TODO Auto-generated method stub
		return "010203040506";
	}

	public byte[] encrypt(byte[] buffer, String key) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return buffer;
	}

	public byte[] decrypt(byte[] buffer, String key) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return buffer;
	}

	public String digest(String mesg) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return "N/A";
	}

	public String digest(byte[] buffer) throws EncryptExcepton {
		return "N/A";
	}

}
