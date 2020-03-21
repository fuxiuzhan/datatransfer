package com.fxz.service.DataTransferServer.Compress.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.fxz.service.DataTransferServer.Compress.AbstractCompress;
import com.fxz.service.DataTransferServer.Compress.CompressExceptions;
import com.fxz.service.DataTransferServer.Compress.ICompress;

import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

/** 
 * @ClassName: Lz4Compress 
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月1日 上午8:59:39  
 */
public class Lz4Compress extends AbstractCompress implements ICompress {

	@Override
	public byte[] compress(byte[] buffer) throws CompressExceptions {
		try {
			LZ4Factory factory = LZ4Factory.fastestInstance();
			ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			LZ4Compressor compressor = factory.fastCompressor();
			LZ4BlockOutputStream compressedOutput = new LZ4BlockOutputStream(byteOutput, 2048, compressor);
			compressedOutput.write(buffer);
			compressedOutput.close();
			return byteOutput.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			throw new CompressExceptions(e);
		}

	}

	@Override
	public byte[] decompress(byte[] buffer) throws CompressExceptions {
		try {
			LZ4Factory factory = LZ4Factory.fastestInstance();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			LZ4FastDecompressor decompresser = factory.fastDecompressor();
			LZ4BlockInputStream lzis = new LZ4BlockInputStream(new ByteArrayInputStream(buffer), decompresser);
			int count;
			byte[] rez = new byte[2048];
			while ((count = lzis.read(rez)) != -1) {
				baos.write(rez, 0, count);
			}
			lzis.close();
			return baos.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			throw new CompressExceptions(e);
		}
	}

}
