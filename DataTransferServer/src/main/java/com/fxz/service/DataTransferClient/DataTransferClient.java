/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.server 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午5:44:16 
 * 
 */
package com.fxz.service.DataTransferClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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
import com.fxz.service.DataTransferServer.Const.Const;
import com.fxz.service.DataTransferServer.Messages.BaseMessage;
import com.fxz.service.DataTransferServer.Messages.DataMessage;
import com.fxz.service.DataTransferServer.Messages.ResultMessage;
import com.fxz.service.DataTransferServer.Messages.VerifyMessage;
import com.fxz.service.DataTransferServer.Statistic.ClientConfig;
import com.fxz.service.DataTransferServer.Statistic.InitConfig;
import com.fxz.service.DataTransferServer.Utils.ClientInfo;
import com.fxz.service.DataTransferServer.Utils.ClientParams;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * @ClassName: FtsClient
 * @Description:
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午5:44:16
 */

public class DataTransferClient extends Thread {
	String file;
	Logger logger = Logger.getLogger(DataTransferClient.class);
	AuthConfig authConfig;
	String ip;
	ConcurrentHashMap<String, String> cMap;
	int port;
	String lSocletId;
	List<Thread> listenlist = new ArrayList<>();

	public DataTransferClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public DataTransferClient(String ip, int port, AuthConfig authConfig) {
		this.ip = ip;
		this.port = port;
		this.authConfig = authConfig;
	}

	private void closeAllListener() {
		for (Thread listener : listenlist) {
			try {
				listener.stop();
			} catch (Exception e) {
			}
		}
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
					ch.pipeline().addLast(new CompressHandler(CompressFactory.getCompressor("snappy")));
					ch.pipeline().addLast(new AuthHandler(authConfig));
					ch.pipeline().addLast(new IdleStateHandler(0, 600, 0, TimeUnit.SECONDS));
					ch.pipeline().addLast(new HeartBeatHandler());
					ch.pipeline().addLast(new ChannelHandlerAdapter() {
						@Override
						public void channelActive(ChannelHandlerContext ctx) throws Exception {
							ClientInfo clientinfo = new ClientInfo();
							clientinfo.setAppID(ClientConfig.getUser().getAppId());
							clientinfo.setHostIP("127.0.0.2");
							clientinfo.setHostName("fuled-pc");
							clientinfo.setHostSysType("windows");
							clientinfo.setMac("00-00-00-00-00-00");
							clientinfo.setMemSize(10240000L);
							clientinfo.setUserID(ClientConfig.getUser().getUserId());
							clientinfo.setClientID(ClientConfig.getUser().getClientId());
							ClientParams.setAppId(clientinfo.getAppID());
							ClientParams.setUserId(clientinfo.getUserID());
							ctx.channel().writeAndFlush(new VerifyMessage(clientinfo));
						}

						@Override
						public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
							logger.error("Error->" + cause.getLocalizedMessage());
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
											for (String portstr : ClientConfig.getPortList()) {
												// portlist2.add(client_id + ":"
												// + local_port + ":" +
												// remote_port);
												ClientListener listener = new ClientListener(Integer.parseInt(portstr.split(":")[1]), portstr.split(":")[0], portstr.split(":")[3],
														Integer.parseInt(portstr.split(":")[2]));
												listener.start();
												listenlist.add(listener);
												logger.info("Listener Started!");
											}

										} else {
											logger.error("Access Deny...");
										}
										break;
									case Const.RTN_CONNECT:
										if (resultMessage.isSucc()) {
											logger.info("new connection created!");
										} else {
											Channel channel = ClientParams.getChannel(resultMessage.getExtendMsg().split("\\|")[0]);
											logger.info("RECV ERROR MESSAGE READS->" + resultMessage + "  ExtendsMsg->" + resultMessage.getExtendMsg());
											if (channel != null) {
												channel.close();
												logger.info("channel->" + resultMessage.getExtendMsg() + "  closed..");
											}
											logger.info("Open Remote Port Failed!");
										}
										break;
									case Const.RTN_ERROR:
										// 处理链路错误
										String lsocket = resultMessage.getExtendMsg().split("\\|")[0];
										Channel channel = ClientParams.getChannel(lsocket);
										logger.info("RECV ERROR MESSAGE READS->" + resultMessage + "  ExtendsMsg->" + resultMessage.getExtendMsg());
										if (channel != null) {
											channel.close();
											logger.info("channel->" + lsocket + "  closed..");
										}
										break;
									default:
										break;
									}
									break;
								case Const.MSG_CTRL:
									// Ctrl Message
									break;
								case Const.MSG_DATA:
									// transfer dataBlock
									DataMessage dataMessage = new DataMessage((BaseMessage) msg);
									Channel channel = ClientParams.getChannel(dataMessage.getlSocketId());
									if (channel != null) {
										channel.writeAndFlush(Unpooled.copiedBuffer(dataMessage.getBody()));
									} else {
										logger.error("can not found channel ->" + dataMessage.getlSocketId());
									}
									break;
								case Const.MSG_TEXT:
									// Text Message
									break;
								case Const.MSG_VERIFY:
									// useless for client
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
			logger.info("Client Started!...");
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			logger.error("Error->" + e.getLocalizedMessage());
		} finally {
			group.shutdownGracefully();
			closeAllListener();
			logger.info("close all listener...");
			logger.info("exit..");
			try {
				Thread.sleep(ClientConfig.getReTryInternal());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
			new DataTransferClient(ClientConfig.getServerIP(), ClientConfig.getServerPort(), ClientConfig.getAuthConfig()).start();
			logger.info("try restart");
		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Need a Config File to Start.... ");
			return;
		}
		System.out.println("Starting Init Configs...." + args[0]);
		if (!InitConfig.readClientConfig(args[0])) {
			System.out.println("Config File Error,Please Check config file->" + args[0]);
			return;
		}
		PropertyConfigurator.configure("config/log4j.properties");
		new DataTransferClient(ClientConfig.getServerIP(), ClientConfig.getServerPort(), ClientConfig.getAuthConfig()).start();
	}
}
