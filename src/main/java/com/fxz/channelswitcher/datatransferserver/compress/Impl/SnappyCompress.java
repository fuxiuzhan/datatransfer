package com.fxz.channelswitcher.datatransferserver.compress.Impl;

import com.fxz.channelswitcher.datatransferserver.compress.AbstractCompress;
import com.fxz.channelswitcher.datatransferserver.compress.CompressExceptions;
import com.fxz.channelswitcher.datatransferserver.compress.ICompress;
import org.xerial.snappy.Snappy;



/**
 * @ClassName: SnappyCompress
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月1日 上午8:59:29
 */
public class SnappyCompress extends AbstractCompress implements ICompress {

	@Override
	public byte[] compress(byte[] buffer) throws CompressExceptions {
		try {
			return Snappy.compress(buffer);
		} catch (Exception e) {
			// TODO: handle exception
			throw new CompressExceptions(e);
		}
	}

	@Override
	public byte[] decompress(byte[] buffer) throws CompressExceptions {
		try {
			return Snappy.uncompress(buffer);
		} catch (Exception e) {
			// TODO: handle exception
			throw new CompressExceptions(e);
		}
	}

}
