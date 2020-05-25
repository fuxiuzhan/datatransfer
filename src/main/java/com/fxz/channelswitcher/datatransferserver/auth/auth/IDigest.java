package com.fxz.channelswitcher.datatransferserver.auth.auth;

import com.fxz.channelswitcher.datatransferserver.auth.exceptions.EncryptExcepton;

/**
 * @ClassName: IDigest
 * @Description: TODO
 * @author: Administrator
 * @date: 2018年1月25日 下午2:06:47
 */
public interface IDigest {
	public String digest(String mesg) throws EncryptExcepton;

	public String digest(byte[] buffer) throws EncryptExcepton;
}
