package com.fxz.service.DataTransferServer.Auth.exceptions;

/**
 * @ClassName: EncryptExcepton
 * @Description: TODO
 * @author: Administrator
 * @date: 2018年1月25日 下午2:14:33
 */
public class EncryptExcepton extends Exception {

	/**
	 * @Fields serialVersionUID : TODO
	 */

	private static final long serialVersionUID = 1L;

	public EncryptExcepton(String exstr) {
		// TODO Auto-generated constructor stub
		super(exstr);
	}

	public EncryptExcepton(String exstr, Throwable e) {
		// TODO Auto-generated constructor stub
		super(exstr, e);
	}

	public EncryptExcepton(Throwable e) {
		// TODO Auto-generated constructor stub
		super(e);
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}

	@Override
	public Throwable getCause() {
		// TODO Auto-generated method stub
		return super.getCause();
	}

}
