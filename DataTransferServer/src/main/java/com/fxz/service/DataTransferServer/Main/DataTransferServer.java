/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.server 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午5:44:05 
 * 
 */
package com.fxz.service.DataTransferServer.Main;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLEngine;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.fxz.service.DataTransferServer.Auth.SSL.SSLConfig;
import com.fxz.service.DataTransferServer.Auth.config.AuthConfig;
import com.fxz.service.DataTransferServer.Auth.handler.AuthHandler;
import com.fxz.service.DataTransferServer.Auth.handler.CheckSumHandler;
import com.fxz.service.DataTransferServer.Auth.handler.CompressHandler;
import com.fxz.service.DataTransferServer.Auth.handler.HeartBeatHandler;
import com.fxz.service.DataTransferServer.Codec.Message2BytesCodec;
import com.fxz.service.DataTransferServer.Compress.CompressFactory;
import com.fxz.service.DataTransferServer.Const.Params;
import com.fxz.service.DataTransferServer.Ctrl.UserCtrl;
import com.fxz.service.DataTransferServer.Handler.HandlerFactory;
import com.fxz.service.DataTransferServer.Messages.BaseMessage;
import com.fxz.service.DataTransferServer.Messages.Handler.IProcessMessage;
import com.fxz.service.DataTransferServer.Statistic.InitConfig;
import com.fxz.service.DataTransferServer.Statistic.ServerConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

/**
 * @ClassName: FtsServer
 * @Description: 命令工厂与底层通讯中间件的交汇处
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午5:44:05
 */

public class DataTransferServer extends Thread {
	int port;
	String ip = "0.0.0.0";
	Logger logger = Logger.getLogger(DataTransferServer.class);
	AuthConfig authConfig;
	AttributeKey<String> socketuuid = AttributeKey.valueOf("socketuuid");
	Attribute<String> socketUUId;
	AttributeKey<String> clientid = AttributeKey.valueOf("clientid");
	Attribute<String> clientId;

	public DataTransferServer(int port) {
		this.port = port;
		authConfig = new AuthConfig();
	}

	public DataTransferServer(int port, AuthConfig auconfig) {
		this.port = port;
		this.authConfig = auconfig;
	}

	public DataTransferServer(String ip, int port) {
		this.ip = ip;
		this.port = port;
		this.authConfig = new AuthConfig();
	}

	public DataTransferServer(String ip, int port, AuthConfig auconfig) {
		this.ip = ip;
		this.port = port;
		this.authConfig = auconfig;
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Need a Config File to Start.... ");
			return;
		}
		System.out.println("Starting Init Configs...." + args[0]);
		if (!InitConfig.readServerConfig(args[0])) {
			System.out.println("Config File Error,Please Check config file->" + args[0]);
			return;
		}
		System.out.println("Import UserInfos....");
		UserCtrl.addUserList(ServerConfig.getUserList());
		System.out.println("Import " + ServerConfig.getUserList().size() + " Users");
		PropertyConfigurator.configure("config/log4j.properties");
		new DataTransferServer(ServerConfig.getBindIP(), ServerConfig.getBindPort(), ServerConfig.getAuthConfig()).start();
	}

	@Override
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_TIMEOUT, 1000).handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							SSLEngine sslEngine = SSLConfig.getSSLContext("config/fxz_test.jks").createSSLEngine();
							sslEngine.setUseClientMode(false);
							sslEngine.setNeedClientAuth(false);
							ch.pipeline().addLast("ssl", new SslHandler(sslEngine));
							ch.pipeline().addLast(new Message2BytesCodec());
							authConfig.setServer(true);
							if (!authConfig.getMessageDigest().equalsIgnoreCase("none")) {
								ch.pipeline().addLast(new CheckSumHandler(authConfig.getMessageDigest()));
							}
							ch.pipeline().addLast(new CompressHandler(CompressFactory.getCompressor("snappy")));
							ch.pipeline().addLast(new AuthHandler(authConfig));
							ch.pipeline().addLast(new IdleStateHandler(600, 0, 0, TimeUnit.SECONDS));
							ch.pipeline().addLast(new HeartBeatHandler());
							ch.pipeline().addLast(new ChannelHandlerAdapter() {
								@Override
								public void channelActive(ChannelHandlerContext ctx) throws Exception {
									// TODO Auto-generated method stub
									System.out.println("channelActive");
									super.channelActive(ctx);
								}

								@Override
								public void channelInactive(ChannelHandlerContext ctx) throws Exception {
									// TODO Auto-generated method stub
									System.out.println("channelInactive");
									clientId = ctx.attr(clientid);
									Params.removeOnlineClient(clientId.get());
									logger.info("Client->" + clientId.get() + "  off line...");
									super.channelInactive(ctx);
								}

								@Override
								public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
									// TODO Auto-generated method stub
									if (msg instanceof BaseMessage) {
										/*
										 * 使用命令工厂处理收到的报文
										 */
										IProcessMessage processMessage = HandlerFactory.getProcessor(((BaseMessage) msg).getMessageType());
										if (processMessage != null) {
											processMessage.process(ctx, (BaseMessage) msg);
										} else {
											logger.error("ErrorMsg->" + msg);
										}

									} else {
										logger.warn("message format error message type is->" + msg.getClass().getName());
									}
									ReferenceCountUtil.release(msg);
								}

								@Override
								public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
									// TODO Auto-generated method stub
									/*
									 * 一旦主链路出现问题，更新在线客户端数据
									 */
									clientId = ctx.attr(clientid);
									Params.removeOnlineClient(clientId.get());
									logger.info("Client->" + clientId.get() + "  off line...");
									ctx.close();
								}

								@Override
								public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
									// TODO Auto-generated method stub
									System.out.println("disconnect");
									System.out.println("channelInactive");
									clientId = ctx.attr(clientid);
									Params.removeOnlineClient(clientId.get());
									logger.info("Client->" + clientId.get() + "  off line...");
								}

							});
						}

					}).option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true);
			System.out.println("Listen Starting");
			ChannelFuture f = b.bind(ip, port).sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}

	}
}
