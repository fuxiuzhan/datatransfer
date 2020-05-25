package com.fxz.channelswitcher.datatransferserver.auth.auth;

import com.fxz.channelswitcher.datatransferserver.auth.exceptions.EncryptExcepton;

/**
 * @ClassName: INonSymEncrypt
 * @Description: TODO
 * @author: Administrator
 * @date: 2018年1月25日 下午2:08:24
 */
public interface INonSymEncrypt {
	public void init(int block) throws EncryptExcepton, EncryptExcepton;

	public void setKeys(String privatekey, String publickey);

	public byte[] encryptByPrivateKey(byte[] buffer, String privateKey) throws EncryptExcepton;

	public byte[] encryptByPublicKey(byte[] buffer, String publicKey) throws EncryptExcepton;

	public byte[] decryptByPrivateKey(byte[] buffer, String privateKey) throws EncryptExcepton;

	public byte[] decryptByPublicKey(byte[] buffer, String publicKey) throws EncryptExcepton;

	public String getPrivateKey();

	public String getPublicKey();
}
