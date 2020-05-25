package com.fxz.channelswitcher.datatransferserver.auth.handler;

import com.fxz.channelswitcher.datatransferserver.compress.ICompress;
import com.fxz.channelswitcher.datatransferserver.messages.BaseMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.apache.log4j.Logger;

/**
 * @ClassName: ComppressHandler
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2019年9月21日 下午5:10:57
 */
public class CompressHandler extends ChannelHandlerAdapter {

	private ICompress compressHandler;
	Logger logger=Logger.getLogger(CompressHandler.class);
	public CompressHandler(ICompress compressHandler) {
		// TODO Auto-generated constructor stub
		this.compressHandler = compressHandler;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof BaseMessage) {
			BaseMessage baseMessage = (BaseMessage) msg;
			baseMessage.setBody(compressHandler.decompress(baseMessage.getBody()));
		}
		super.channelRead(ctx, msg);

	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof BaseMessage) {
			BaseMessage baseMessage = (BaseMessage) msg;
			baseMessage.setBody(compressHandler.compress(baseMessage.getBody()));
		}
		super.write(ctx, msg, promise);
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
		logger.error(cause);
	}

}
