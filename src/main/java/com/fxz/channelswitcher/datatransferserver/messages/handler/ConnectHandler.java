package com.fxz.channelswitcher.datatransferserver.messages.handler;

import com.fxz.channelswitcher.datatransferserver.constant.Const;
import com.fxz.channelswitcher.datatransferserver.constant.Params;
import com.fxz.channelswitcher.datatransferserver.messages.BaseMessage;
import com.fxz.channelswitcher.datatransferserver.messages.ConnectMessage;
import com.fxz.channelswitcher.datatransferserver.messages.ResultMessage;
import com.fxz.channelswitcher.datatransferserver.utils.ClientInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: ConnectHandler
 * @Description: ConnectMessage LcoalServer收到此类报文会进行本地连接，连接成功后将产生ChannelPare
 *               也就是客户端的连接正常建立
 *               （在Server并不会产生新的连接，新的连接在客户端一层产生，新连接并不会直接体现在Server端，但对Server端
 *               产生影响，Server端会产生新的ChannelPare）SocketUUID是唯一标识一个ChannelPare的值，
 *               此值在主动发起连接一方生成，并且在 整个通讯过程中不变，断线重连和状态恢复需要SocketUUID值的支持
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月12日 下午3:08:40
 */
public class ConnectHandler implements IProcessMessage {
	Logger logger = Logger.getLogger(this.getClass());
	AttributeKey<String> socketuuid = AttributeKey.valueOf("socketuuid");
	Attribute<String> socketUUId;

	@Override
	public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws IOException {
		// TODO Auto-generated method stub
		/*
		 * ConnectHandler处理逻辑： 因为能开始处理ConnectMessage意味着已经通过验证，所以不必考虑客户端的合法性检查
		 * 1、首先检查目标方是否存在，存在进入第二部 2、检查目标方是否在线，在线转入第三步
		 * 3、发送ConnectMessage开始请求，转入第四部 4、收到对应SocketUUID 连接回应进行转发
		 * 
		 * 如果任何一部出错，就返回对应的错误描述
		 */
		ConnectMessage connectMessage = new ConnectMessage((BaseMessage) baseMessage);
		ClientInfo clientinfo = Params.getClientInfo(connectMessage.getClientId());
		if (clientinfo != null) {
			Channel targetChannel = Params.getOnlineClient(connectMessage.getClientId());
			if (Params.getOnlineClient(connectMessage.getClientId()) != null) {
				ConnectMessage tconnectMessage = new ConnectMessage(connectMessage.getIp(), connectMessage.getPort(), connectMessage.getSocketUUID(), connectMessage.getClientId(),
						connectMessage.getlSocketId());
				targetChannel.writeAndFlush(tconnectMessage);
				Map<Channel, Channel> channelPare = new HashMap<>();
				channelPare.put(ctx.channel(), targetChannel);
				Params.addChannelPare(connectMessage.getSocketUUID(), channelPare);
				socketUUId = ctx.attr(socketuuid);
				socketUUId.set(connectMessage.getSocketUUID());
				logger.info("connect to " + connectMessage.getClientId());
			} else {
				ResultMessage resultMessage = new ResultMessage(Const.RTN_CONNECT, connectMessage.getlSocketId() + "|client not online", false);
				resultMessage.setSocketUUID(((BaseMessage) baseMessage).getSocketUUID());
				ctx.writeAndFlush(resultMessage);
			}
		} else {
			ResultMessage resultMessage = new ResultMessage(Const.RTN_CONNECT, connectMessage.getlSocketId() + "|no such client", false);
			resultMessage.setSocketUUID(((BaseMessage) baseMessage).getSocketUUID());
			ctx.writeAndFlush(resultMessage);
		}
	}

}
