package com.fxz.service.DataTransferServer.Compress;

/** 
 * @ClassName: CompressExceptions 
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月1日 上午8:57:07  
 */
public class CompressExceptions extends Exception {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	public CompressExceptions(Throwable cause) {
		super(cause);
	}

	public CompressExceptions(String exstr) {
		super(exstr);
	}

	public CompressExceptions(String exstr, Throwable e) {
		super(exstr, e);
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}
}
