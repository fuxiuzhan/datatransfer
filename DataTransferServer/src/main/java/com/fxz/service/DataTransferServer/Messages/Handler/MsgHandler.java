package com.fxz.service.DataTransferServer.Messages.Handler;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import com.fxz.service.DataTransferServer.Messages.BaseMessage;

/**
 * @ClassName: MsgHandler
 * @Description: 实现消息报文的转发和服务器广播的场景
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月12日 下午2:04:32
 */
public class MsgHandler implements IProcessMessage {

	@Override
	public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws IOException {
		// TODO Auto-generated method stub

	}

}
