package com.fxz.channelswitcher.datatransferserver.compress.Impl;

import com.fxz.channelswitcher.datatransferserver.compress.AbstractCompress;
import com.fxz.channelswitcher.datatransferserver.compress.CompressExceptions;
import com.fxz.channelswitcher.datatransferserver.compress.ICompress;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;



/** 
 * @ClassName: DeflaterCompress 
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月1日 上午8:59:48  
 */
public class DeflaterCompress extends AbstractCompress implements ICompress {

	@Override
	public byte[] compress(byte[] buffer) throws CompressExceptions {
		try {
			Deflater deflater = new Deflater();
			deflater.setLevel(1);
			deflater.setInput(buffer);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(8192);
			deflater.finish();
			while (!deflater.finished()) {
				byte[] rez = new byte[8192];
				int count = deflater.deflate(rez);
				outputStream.write(rez, 0, count);
			}
			outputStream.close();
			byte[] output = outputStream.toByteArray();
			return output;
		} catch (Exception e) {
			// TODO: handle exception
			throw new CompressExceptions(e);
		}
	}

	@Override
	public byte[] decompress(byte[] buffer) throws CompressExceptions {
		try {
			Inflater inflater = new Inflater();
			inflater.setInput(buffer);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(8192);
			while (!inflater.finished()) {
				byte[] rez = new byte[8192];
				int count = inflater.inflate(rez);
				outputStream.write(rez, 0, count);
			}
			outputStream.close();
			byte[] output = outputStream.toByteArray();
			return output;
		} catch (Exception e) {
			// TODO: handle exception
			throw new CompressExceptions(e);
		}
	}

}
