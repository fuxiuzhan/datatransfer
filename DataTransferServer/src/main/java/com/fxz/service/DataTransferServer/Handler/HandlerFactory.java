package com.fxz.service.DataTransferServer.Handler;

import java.util.HashMap;
import java.util.Map;

import com.fxz.service.DataTransferServer.Const.Const;
import com.fxz.service.DataTransferServer.Messages.Handler.ConnectHandler;
import com.fxz.service.DataTransferServer.Messages.Handler.CtrlHandler;
import com.fxz.service.DataTransferServer.Messages.Handler.DataHandler;
import com.fxz.service.DataTransferServer.Messages.Handler.IProcessMessage;
import com.fxz.service.DataTransferServer.Messages.Handler.MsgHandler;
import com.fxz.service.DataTransferServer.Messages.Handler.ResultHandler;
import com.fxz.service.DataTransferServer.Messages.Handler.VerifyHandler;

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
