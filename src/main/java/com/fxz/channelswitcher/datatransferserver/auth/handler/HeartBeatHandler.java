/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.auth.handler 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年9月18日 上午9:19:44 
 * 
 */
package com.fxz.channelswitcher.datatransferserver.auth.handler;

import com.fxz.channelswitcher.datatransferserver.constant.Const;
import com.fxz.channelswitcher.datatransferserver.messages.BaseMessage;
import com.fxz.channelswitcher.datatransferserver.messages.HeartBeatMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;

/**
 * @ClassName: HeartBeatHandler
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年9月18日 上午9:19:44
 */

public class HeartBeatHandler extends ChannelHandlerAdapter {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				logger.info("READER_IDLE " + "Peer Remote Address->" + ctx.channel().remoteAddress());
				ctx.writeAndFlush(new HeartBeatMessage());
				logger.info("Send HeartBeat" + "Peer Remote Address->" + ctx.channel().remoteAddress());
			}
			if (event.state() == IdleState.WRITER_IDLE) {
				logger.info("WRITER_IDLE" + "Peer Remote Address->" + ctx.channel().remoteAddress());
				ctx.writeAndFlush(new HeartBeatMessage());
				logger.info("Send HeartBeat" + "Peer Remote Address->" + ctx.channel().remoteAddress());
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		if (((BaseMessage) msg).getMessageType() == Const.AUTH_HEARTBEAT) {
			ReferenceCountUtil.release(msg);
		} else {
			super.channelRead(ctx, msg);
		}
	}

}
