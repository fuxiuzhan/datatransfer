package com.fxz.service.DataTransferServer.Compress;

/** 
 * @ClassName: ICompress 
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月1日 上午8:57:02  
 */
public interface ICompress {
	byte[] compress(byte[] buffer) throws CompressExceptions;

	byte[] decompress(byte[] buffer) throws CompressExceptions;
}
