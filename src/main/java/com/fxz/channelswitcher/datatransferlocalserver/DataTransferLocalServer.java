package com.fxz.channelswitcher.datatransferlocalserver;

/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.server 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午5:44:16 
 * 
 */

import com.fxz.channelswitcher.datatransferserver.auth.config.AuthConfig;
import com.fxz.channelswitcher.datatransferserver.auth.handler.AuthHandler;
import com.fxz.channelswitcher.datatransferserver.auth.handler.CheckSumHandler;
import com.fxz.channelswitcher.datatransferserver.auth.handler.CompressHandler;
import com.fxz.channelswitcher.datatransferserver.auth.handler.HeartBeatHandler;
import com.fxz.channelswitcher.datatransferserver.auth.ssl.SSLConfig;
import com.fxz.channelswitcher.datatransferserver.codec.Message2BytesCodec;
import com.fxz.channelswitcher.datatransferserver.compress.CompressFactory;
import com.fxz.channelswitcher.datatransferserver.constant.Const;
import com.fxz.channelswitcher.datatransferserver.messages.*;
import com.fxz.channelswitcher.datatransferserver.statistic.InitConfig;
import com.fxz.channelswitcher.datatransferserver.statistic.LocalServerConfig;
import com.fxz.channelswitcher.datatransferserver.utils.ClientInfo;
import com.fxz.channelswitcher.datatransferserver.utils.ClientParams;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLEngine;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: FtsClient
 * @Description:
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午5:44:16
 */

public class DataTransferLocalServer extends Thread {
	String file;
	Logger logger = Logger.getLogger(DataTransferLocalServer.class);
	AuthConfig authConfig;
	String ip;
	int port;

	public DataTransferLocalServer(String ip, int port, AuthConfig authConfig) {
		this.ip = ip;
		this.port = port;
		this.authConfig = authConfig;
	}

	@Override
	public void run() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_LINGER, 0).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					SSLEngine sslEngine = SSLConfig.getSSLContext("config/fxz_test.jks").createSSLEngine();
					sslEngine.setUseClientMode(true);
					sslEngine.setNeedClientAuth(true);
					ch.pipeline().addLast("ssl", new SslHandler(sslEngine));
					ch.pipeline().addLast(new Message2BytesCodec());
					if (!authConfig.getMessageDigest().equalsIgnoreCase("none")) {
						ch.pipeline().addLast(new CheckSumHandler(authConfig.getMessageDigest()));
					}
					//ch.pipeline().addLast(new CompressHandler(CompressFactory.getCompressor("snappy")));
					ch.pipeline().addLast(new AuthHandler(authConfig));
					ch.pipeline().addLast(new IdleStateHandler(30, 30, 0, TimeUnit.SECONDS));
					ch.pipeline().addLast(new HeartBeatHandler());
					ch.pipeline().addLast(new ChannelHandlerAdapter() {
						@Override
						public void channelActive(ChannelHandlerContext ctx) throws Exception {
							ClientInfo clientinfo = new ClientInfo();
							clientinfo.setAppID(LocalServerConfig.getUser().getAppId());
							clientinfo.setHostIP("127.0.0.2");
							clientinfo.setHostName("fuled-pc");
							clientinfo.setHostSysType("windows");
							clientinfo.setMac("00-00-00-00-00-00");
							clientinfo.setMemSize(10240000L);
							clientinfo.setUserID(LocalServerConfig.getUser().getUserId());
							// clientinfo.setClientID(clientinfo.getHostName() +
							// "-" + Utils.digest(clientinfo.getMac()));
							clientinfo.setClientID(LocalServerConfig.getUser().getClientId());
							ClientParams.setAppId(clientinfo.getAppID());
							ClientParams.setUserId(clientinfo.getUserID());
							ctx.channel().writeAndFlush(new VerifyMessage(clientinfo));
						}

						@Override
						public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
							logger.error("Error->" + cause.getLocalizedMessage());
						}

						@Override
						public void channelInactive(ChannelHandlerContext ctx) throws Exception {
							// TODO Auto-generated method stub
							super.channelInactive(ctx);
						}

						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
							// TODO Auto-generated method stub
							if (msg instanceof BaseMessage) {
								switch (((BaseMessage) msg).getMessageType()) {
								case Const.MSG_REULST:
									ResultMessage resultMessage = new ResultMessage((BaseMessage) msg);
									switch (resultMessage.getRsult_type()) {
									case Const.RTN_VERIFY:
										if (resultMessage.isSucc()) {
											logger.info("Access Permit...");
											ClientParams.setMainChannel(ctx.channel());
										} else {
											logger.error("Access Deny...");
										}
										break;
									case Const.RTN_CONNECT:
										logger.info("get ErrorMessage->" + msg);
										break;
									case Const.RTN_ERROR:
										ResultMessage resultMessage_r = new ResultMessage((BaseMessage) msg);
										String lsocket = resultMessage_r.getExtendMsg().split("\\|")[0];
										Channel channel_e = ClientParams.getChannel(lsocket);
										logger.info("RECV ERROR MESSAGE READS->" + resultMessage_r + "  ExtendsMsg->" + resultMessage_r.getExtendMsg());
										if (channel_e != null) {
											channel_e.close();
											logger.info("channel->" + lsocket + "  closed..");
										}
									}
									break;
								case Const.MSG_CTRL:
									// Ctrl Message
									break;
								case Const.MSG_DATA:
									// transfer dataBlock
									DataMessage dataMessage = new DataMessage((BaseMessage) msg);
									Channel channel = ClientParams.getChannel(dataMessage.getlSocketId());
									if (channel == null) {
										Thread.sleep(1000);
									}
									channel = ClientParams.getChannel(dataMessage.getlSocketId());
									if (channel != null) {
										channel.writeAndFlush(Unpooled.copiedBuffer(dataMessage.getBody()));
									} else {
										logger.error("can not found channelpare ... lSocketId->" + dataMessage.getlSocketId());
										ResultMessage resultMessage2 = new ResultMessage(Const.RTN_ERROR, dataMessage.getlSocketId() + "|can not connect", false);
										ctx.channel().writeAndFlush(resultMessage2);
										logger.info("send error resultMessage");
									}
									break;
								case Const.MSG_TEXT:
									// Text Message
									break;
								case Const.MSG_VERIFY:
									// useless for client
									break;

								case Const.MSG_CONNECT:
									try {
										ConnectMessage connectMessage = new ConnectMessage((BaseMessage) msg);
										ClientConnector connector = new ClientConnector(connectMessage.getIp(), connectMessage.getPort(), connectMessage);
										connector.start();
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
										System.out.println("error->" + e.getLocalizedMessage());
									}
									break;
								}
							} else {
								logger.warn("message format error message type->" + msg.getClass().getName());
							}
							ReferenceCountUtil.release(msg);
						}
					});
				}

			});

			final ChannelFuture f = b.connect(ip, port).sync();
			logger.info("LocalServer Started!...");
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			logger.error("Error->" + e.getLocalizedMessage());
		} finally {
			group.shutdownGracefully();
			logger.info("exit..");
			try {
				Thread.sleep(LocalServerConfig.getReTryInternal());
			} catch (InterruptedException e) {
			}
			new DataTransferLocalServer(LocalServerConfig.getServerIP(), LocalServerConfig.getServerPort(), LocalServerConfig.getAuthConfig()).start();
			logger.info("try restart...");
		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Need a Config File to Start.... ");
			return;
		}
		System.out.println("Starting Init Configs...." + args[0]);
		if (!InitConfig.readLocalServerConfig(args[0])) {
			System.out.println("Config File Error,Please Check config file->" + args[0]);
			return;
		}
		//PropertyConfigurator.configure($ContentRoot$+"/config/log4j.properties");
		new DataTransferLocalServer(LocalServerConfig.getServerIP(), LocalServerConfig.getServerPort(), LocalServerConfig.getAuthConfig()).start();
	}
}
