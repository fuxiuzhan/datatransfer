/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.config 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午4:17:23 
 * 
 */
package com.fxz.channelswitcher.datatransferserver.constant;

/**
 * @ClassName: Const
 * @Description: 报文类型常量表
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午4:17:23
 */

public class Const {
	/*
	 * 报文常量类型 MSG_OP 0x0~0x0f保留
	 */
	public static final byte MSG_CTRL = 0x10;
	public static final byte MSG_DATA = 0x11;
	public static final byte MSG_TEXT = 0x12;
	public static final byte MSG_REULST = 0x13;
	public static final byte MSG_VERIFY = 0x14;
	public static final byte MSG_CONNECT = 0x15;

	// ResultMessage 内部类型
	public static final byte RTN_VERIFY = 0x20;
	public static final byte RTN_CONNECT = 0x21;
	public static final byte RTN_ERROR = 0x22;
	/*
	 * 认证报文常量类型
	 */

	// auth
	public static final byte MESG_TYPE_AUTH = 0x0c;
	public static final byte AUTH_PUBLIC_KEY = 0x01;
	public static final byte AUTH_PRIVATE_KEY = 0x02;
	public static final byte AUTH_DH_MESSAGE = 0x03;
	public static final byte AUTH_NEED_HD = 0x04;
	public static final byte AUTH_READY = 0x05;
	public static final byte AUTH_HEARTBEAT = 0x06;

}
