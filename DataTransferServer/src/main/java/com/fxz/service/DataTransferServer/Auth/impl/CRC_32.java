package com.fxz.service.DataTransferServer.Auth.impl;

import java.util.zip.CRC32;

import com.fxz.service.DataTransferServer.Auth.auth.IDigest;
import com.fxz.service.DataTransferServer.Auth.auth.Utils;
import com.fxz.service.DataTransferServer.Auth.exceptions.EncryptExcepton;

public class CRC_32 implements IDigest {
	CRC32 crc32 = new CRC32();

	public String digest(String mesg) throws EncryptExcepton {
		// TODO Auto-generated method stub
		crc32.update(mesg.getBytes());
		return Utils.Long2Hex(crc32.getValue());
	}

	public String digest(byte[] buffer) throws EncryptExcepton {
		// TODO Auto-generated method stub
		crc32.update(buffer);
		return Utils.Long2Hex(crc32.getValue());
	}

}
