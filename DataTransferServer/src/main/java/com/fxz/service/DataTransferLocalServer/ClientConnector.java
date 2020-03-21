package com.fxz.service.DataTransferLocalServer;

import org.apache.log4j.Logger;

import com.fxz.service.DataTransferServer.Const.Const;
import com.fxz.service.DataTransferServer.Messages.ConnectMessage;
import com.fxz.service.DataTransferServer.Messages.DataMessage;
import com.fxz.service.DataTransferServer.Messages.ResultMessage;
import com.fxz.service.DataTransferServer.Utils.ClientParams;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

public class ClientConnector extends Thread {
	private String ip;
	private int port;
	private ConnectMessage connectMessage;
	AttributeKey<String> lsock = AttributeKey.valueOf("lsocketId");
	Attribute<String> lSocketId;
	AttributeKey<String> socketuuid = AttributeKey.valueOf("socketuuid");
	Attribute<String> socketUUId;
	Logger logger = Logger.getLogger(this.getClass());

	public ClientConnector(String ip, int port, ConnectMessage connectMessage) {
		this.ip = ip;
		this.port = port;
		this.connectMessage = connectMessage;
	}

	@Override
	public void run() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_LINGER, 0).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ChannelHandlerAdapter() {

						@Override
						public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
							lSocketId = ctx.attr(lsock);
							socketUUId = ctx.attr(socketuuid);
							ResultMessage resultMessage = new ResultMessage(Const.RTN_ERROR, lSocketId.get() + "|" + cause.getLocalizedMessage(), false);
							resultMessage.setSocketUUID(connectMessage.getSocketUUID());
							ClientParams.getMainChannel().writeAndFlush(resultMessage);
							logger.error("Error->" + cause.getLocalizedMessage());
							ClientParams.removeChannel(connectMessage.getSocketUUID());
						}

						@Override
						public void channelActive(ChannelHandlerContext ctx) throws Exception {
							// TODO Auto-generated method stub
							ClientParams.addChannel(connectMessage.getlSocketId(), ctx.channel());
							ResultMessage resultMessage = new ResultMessage(Const.RTN_CONNECT, "access", true);
							resultMessage.setSocketUUID(connectMessage.getSocketUUID());
							ClientParams.getMainChannel().writeAndFlush(resultMessage);
							lSocketId = ctx.attr(lsock);
							lSocketId.set(connectMessage.getlSocketId());
							socketUUId = ctx.attr(socketuuid);
							socketUUId.set(connectMessage.getSocketUUID());
						}

						@Override
						public void channelInactive(ChannelHandlerContext ctx) throws Exception {
							lSocketId = ctx.attr(lsock);
							socketUUId = ctx.attr(socketuuid);
							ResultMessage resultMessage = new ResultMessage(Const.RTN_ERROR, lSocketId.get() + "|" + "disconnect", false);
							resultMessage.setSocketUUID(connectMessage.getSocketUUID());
							ClientParams.getMainChannel().writeAndFlush(resultMessage);
							logger.error("Error->" + "disconnect");
							ClientParams.removeChannel(connectMessage.getSocketUUID());
						}

						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
							// TODO Auto-generated method stub
							if (msg instanceof ByteBuf) {
								ByteBuf buffer = (ByteBuf) msg;
								int len = buffer.readableBytes();
								if (len > 0) {
									lSocketId = ctx.attr(lsock);
									socketUUId = ctx.attr(socketuuid);
									byte[] bytes = new byte[len];
									buffer.readBytes(bytes);
									DataMessage dataMessage = new DataMessage(socketUUId.get(), lSocketId.get(), ClientParams.getUserId(), ClientParams.getAppId(), bytes, true);
									ClientParams.getMainChannel().writeAndFlush(dataMessage);
								}
							}
							ReferenceCountUtil.release(msg);
						}

					});
				}

			});

			final ChannelFuture f = b.connect(ip, port).sync();
			ClientParams.addChannel(connectMessage.getSocketUUID(), f.channel());
			logger.info("Connector Connected!...");
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			ResultMessage resultMessage = new ResultMessage(Const.RTN_CONNECT, connectMessage.getlSocketId() + "|" + e.getLocalizedMessage(), false);
			resultMessage.setSocketUUID(connectMessage.getSocketUUID());
			ClientParams.getMainChannel().writeAndFlush(resultMessage);
			logger.error("Error->" + e.getLocalizedMessage());
		} finally {
			group.shutdownGracefully();
			logger.info("Connector Exit...");
		}

	}
}
