/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.auth.impl 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年9月3日 上午9:38:03 
 * 
 */
package com.fxz.service.DataTransferServer.Auth.impl;

import java.security.MessageDigest;

import com.fxz.service.DataTransferServer.Auth.auth.IDigest;
import com.fxz.service.DataTransferServer.Auth.auth.Utils;
import com.fxz.service.DataTransferServer.Auth.exceptions.EncryptExcepton;

/** 
 * @ClassName: MD5 
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月3日 上午9:38:03  
 */

public class MD5 implements IDigest{

	@Override
	public String digest(String mesg) throws EncryptExcepton {
		// TODO Auto-generated method stub
		return digest(mesg.getBytes());
	}

	@Override
	public String digest(byte[] buffer) throws EncryptExcepton {
		// TODO Auto-generated method stub
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			return Utils.Byte2Hex(md5.digest(buffer));
		} catch (Exception e) {
			throw new EncryptExcepton(e);
		}
	}

}

