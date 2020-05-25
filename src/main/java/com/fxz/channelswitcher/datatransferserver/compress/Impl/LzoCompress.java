package com.fxz.channelswitcher.datatransferserver.compress.Impl;

import com.fxz.channelswitcher.datatransferserver.compress.AbstractCompress;
import com.fxz.channelswitcher.datatransferserver.compress.CompressExceptions;
import com.fxz.channelswitcher.datatransferserver.compress.ICompress;
import org.anarres.lzo.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;



/** 
 * @ClassName: LzoCompress 
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月1日 上午8:59:34  
 */
public class LzoCompress extends AbstractCompress implements ICompress {

	@Override
	public byte[] compress(byte[] buffer) throws CompressExceptions {
		try {
			LzoCompressor compressor = LzoLibrary.getInstance().newCompressor(LzoAlgorithm.LZO1X, null);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			LzoOutputStream cs = new LzoOutputStream(os, compressor);
			cs.write(buffer);
			cs.close();
			return os.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			throw new CompressExceptions(e);
		}
	}

	@Override
	public byte[] decompress(byte[] buffer) throws CompressExceptions {
		// TODO Auto-generated method stub
		try {
			LzoDecompressor decompressor = LzoLibrary.getInstance().newDecompressor(LzoAlgorithm.LZO1X, null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ByteArrayInputStream is = new ByteArrayInputStream(buffer);
			LzoInputStream us = new LzoInputStream(is, decompressor);
			int count;
			byte[] rez = new byte[2048];
			while ((count = us.read(rez)) != -1) {
				baos.write(rez, 0, count);
			}
			us.close();
			return baos.toByteArray();
		} catch (Exception e) {
			throw new CompressExceptions(e);
		}

	}

}
