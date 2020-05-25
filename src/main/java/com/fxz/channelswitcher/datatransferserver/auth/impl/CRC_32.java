package com.fxz.channelswitcher.datatransferserver.auth.impl;

import com.fxz.channelswitcher.datatransferserver.auth.auth.IDigest;
import com.fxz.channelswitcher.datatransferserver.auth.auth.Utils;
import com.fxz.channelswitcher.datatransferserver.auth.exceptions.EncryptExcepton;

import java.util.zip.CRC32;


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
