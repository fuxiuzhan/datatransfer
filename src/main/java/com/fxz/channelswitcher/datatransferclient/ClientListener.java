package com.fxz.channelswitcher.datatransferclient;

import com.fxz.channelswitcher.datatransferserver.constant.Const;
import com.fxz.channelswitcher.datatransferserver.constant.Params;
import com.fxz.channelswitcher.datatransferserver.handler.IpDetectHandler;
import com.fxz.channelswitcher.datatransferserver.messages.ConnectMessage;
import com.fxz.channelswitcher.datatransferserver.messages.DataMessage;
import com.fxz.channelswitcher.datatransferserver.messages.ResultMessage;
import com.fxz.channelswitcher.datatransferserver.utils.ClientParams;
import org.apache.log4j.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

public class ClientListener extends Thread {
    Logger logger = Logger.getLogger(this.getClass());
    private int port;
    private String targetclient;
    private String targetip;
    private int targetport;

    public ClientListener(int port, String tarclient, String tarip, int tarport) {
        this.port = port;
        this.targetclient = tarclient;
        this.targetip = tarip;
        this.targetport = tarport;
    }

    AttributeKey<String> lsock = AttributeKey.valueOf("lsocketId");
    Attribute<String> lSocketId;
    AttributeKey<String> socketuuid = AttributeKey.valueOf("socketuuid");
    Attribute<String> socketUUId;

    @Override
    public void run() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(Params.getGroup(), Params.getWorkGroup()).channel(NioServerSocketChannel.class).option(ChannelOption.SO_TIMEOUT, 1000).handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                        	ch.pipeline().addLast(new IpDetectHandler());
                            ch.pipeline().addLast(new ChannelHandlerAdapter() {
                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    // TODO Auto-generated method stub
                                    /*
                                     * 本地连接检测到子连接出现问题，需要将出问题的连接ID发送给服务端，让服务端断开后半部分连接
                                     * ，防止两边连接不对称，但出现一个问题
                                     * 因为ResultMessage只有从目标端到源端一个方向
                                     * ，但现在的话有从源端到目标端的情况
                                     * ，看来需要扩展ResultMessage，使它可以标识反向
                                     */
                                    ClientParams.removeChannel(ctx.channel().id().toString());
                                    cause.printStackTrace();
                                }

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    // TODO Auto-generated method stub
                                    // 连接建立，逻辑应该是本地建立连接触发远端建立连接，如果建立失败本地也断开，本地连接的管理维护与客户端和服务端的方式不同
                                    // 每次触发这个方法就加入一个新客户端
                                    super.channelActive(ctx);
                                    ConnectMessage connectMessage = new ConnectMessage(targetip, targetport, ctx.channel().id().toString(), targetclient);
                                    ClientParams.getMainChannel().writeAndFlush(connectMessage);
                                    ClientParams.addChannel(connectMessage.getlSocketId(), ctx.channel());
                                    lSocketId = ctx.attr(lsock);
                                    lSocketId.set(connectMessage.getlSocketId());
                                    socketUUId = ctx.attr(socketuuid);
                                    socketUUId.set(connectMessage.getSocketUUID());
                                }

                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                    // TODO Auto-generated method stub
                                    super.channelInactive(ctx);
                                    lSocketId = ctx.attr(lsock);
                                    socketUUId = ctx.attr(socketuuid);
                                    ClientParams.removeChannel(lSocketId.get());
                                    ResultMessage resultMessage = new ResultMessage(Const.RTN_ERROR, lSocketId + "|disconnect", false, true);
                                    resultMessage.setSocketUUID(socketUUId.get());
                                    ClientParams.getMainChannel().writeAndFlush(resultMessage);
                                    System.out.println("Channel Inactive....");
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    // TODO Auto-generated method stub
                                    if (msg instanceof ByteBuf) {
                                        ByteBuf buffer = (ByteBuf) msg;
                                        int len = buffer.readableBytes();
                                        if (len > 0) {
                                            lSocketId = ctx.attr(lsock);
                                            byte[] bytes = new byte[len];
                                            buffer.readBytes(bytes);
                                            DataMessage dataMessage = new DataMessage(ctx.channel().id().toString(), lSocketId.get(), ClientParams.getUserId(), ClientParams.getAppId(), bytes);
                                            ClientParams.getMainChannel().writeAndFlush(dataMessage);
                                        }
                                    }
                                    ReferenceCountUtil.release(msg);
                                }

                            });
                        }

                    }).option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true);
            System.out.println("Listener Starting");
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("Error->" + e.getLocalizedMessage());
        } finally {
        }

    }
}
