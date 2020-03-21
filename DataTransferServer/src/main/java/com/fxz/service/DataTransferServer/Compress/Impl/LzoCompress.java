package com.fxz.service.DataTransferServer.Compress.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.anarres.lzo.LzoAlgorithm;
import org.anarres.lzo.LzoCompressor;
import org.anarres.lzo.LzoDecompressor;
import org.anarres.lzo.LzoInputStream;
import org.anarres.lzo.LzoLibrary;
import org.anarres.lzo.LzoOutputStream;

import com.fxz.service.DataTransferServer.Compress.AbstractCompress;
import com.fxz.service.DataTransferServer.Compress.CompressExceptions;
import com.fxz.service.DataTransferServer.Compress.ICompress;

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
