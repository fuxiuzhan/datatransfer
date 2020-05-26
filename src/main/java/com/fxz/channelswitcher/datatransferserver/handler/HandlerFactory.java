package com.fxz.channelswitcher.datatransferserver.handler;

import com.fxz.channelswitcher.datatransferserver.constant.Const;
import com.fxz.channelswitcher.datatransferserver.messages.handler.*;

import java.util.HashMap;
import java.util.Map;



/**
 * @ClassName: HandlerFactory
 * @Description: 实现Handler工厂
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月12日 上午10:01:37
 */
public class HandlerFactory {
	private static Map<Byte, IProcessMessage> handlerMap = new HashMap<>();
	static {
		handlerMap.put(Const.MSG_CTRL, new CtrlHandler());
		handlerMap.put(Const.MSG_DATA, new DataHandler());
		handlerMap.put(Const.MSG_REULST, new ResultHandler());
		handlerMap.put(Const.MSG_TEXT, new MsgHandler());
		handlerMap.put(Const.MSG_VERIFY, new VerifyHandler());
		handlerMap.put(Const.MSG_CONNECT, new ConnectHandler());
	}

	public static IProcessMessage getProcessor(byte type) {
		return handlerMap.get(type);
	}
}
