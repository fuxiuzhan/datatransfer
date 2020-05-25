package com.fxz.channelswitcher.datatransferserver.compress.Impl;

import com.fxz.channelswitcher.datatransferserver.compress.AbstractCompress;
import com.fxz.channelswitcher.datatransferserver.compress.CompressExceptions;
import com.fxz.channelswitcher.datatransferserver.compress.ICompress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/** 
 * @ClassName: GzipCompress 
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月1日 上午8:59:44  
 */
public class GzipCompress extends AbstractCompress implements ICompress {

	@Override
	public byte[] compress(byte[] buffer) throws CompressExceptions {
		ByteArrayOutputStream out = new ByteArrayOutputStream(8192);
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(buffer);
			gzip.finish();
			return out.toByteArray();
		} catch (IOException e) {
			throw new CompressExceptions(e);
		} finally {
			try {
				gzip.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new CompressExceptions(e);
			}
		}
	}

	@Override
	public byte[] decompress(byte[] buffer) throws CompressExceptions {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
			GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(buffer));
			int count;
			byte[] rez = new byte[8192];
			while ((count = gis.read(rez)) != -1) {
				baos.write(rez, 0, count);
			}
			gis.close();
			return baos.toByteArray();
		} catch (Exception e) {
			throw new CompressExceptions(e);
		}
	}

}
