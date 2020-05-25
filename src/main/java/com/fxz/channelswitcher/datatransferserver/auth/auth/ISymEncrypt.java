package com.fxz.channelswitcher.datatransferserver.auth.auth;

import com.fxz.channelswitcher.datatransferserver.auth.exceptions.EncryptExcepton;

/**
 * @ClassName: ISymEncrypt
 * @Description: TODO
 * @author: Administrator
 * @date: 2018年1月25日 下午2:11:51
 */
public interface ISymEncrypt {
	 byte[] encrypt(byte[] buffer, String key) throws EncryptExcepton, EncryptExcepton;

	 byte[] decrypt(byte[] buffer, String key) throws EncryptExcepton;
}
