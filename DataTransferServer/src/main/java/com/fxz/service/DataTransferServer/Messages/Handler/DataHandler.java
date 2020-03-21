package com.fxz.service.DataTransferServer.Messages.Handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.fxz.service.DataTransferServer.Const.Const;
import com.fxz.service.DataTransferServer.Const.Params;
import com.fxz.service.DataTransferServer.Messages.BaseMessage;
import com.fxz.service.DataTransferServer.Messages.DataMessage;
import com.fxz.service.DataTransferServer.Messages.ResultMessage;

/**
 * @ClassName: DataHandler
 * @Description: 
 *               主要的数据处理Handler，实现对数据的交换,使用Attr来标记各个channel，在channel出现异常断开时，及时清除不完整的连接
 *               ，清除的方式为：找到SocketUUID对应的Map，对KEY和VALUE都发送ERROR的ResultMessage
 *               ，捕获的异常不进行额外处理，最后移除SocketUUID标记
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月12日 下午2:02:20
 */
public class DataHandler implements IProcessMessage {

	Logger logger = Logger.getLogger(this.getClass());
	AttributeKey<String> socketuuid = AttributeKey.valueOf("socketuuid");
	Attribute<String> socketUUId;
	AttributeKey<String> lsock = AttributeKey.valueOf("lsocketId");
	Attribute<String> lSocketId;

	@Override
	public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws IOException {
		// TODO Auto-generated method stub
		DataMessage dataMessage = new DataMessage((BaseMessage) baseMessage);
		Map<Channel, Channel> channelPare = Params.getChannelPare(dataMessage.getSocketUUID());//
		if (channelPare != null) {
			for (Entry<Channel, Channel> channelFrom : channelPare.entrySet()) {
				DataMessage dataMessage2 = new DataMessage(dataMessage.getSocketUUID(), dataMessage.getlSocketId(), baseMessage.getUserID(), baseMessage.getAppID(), dataMessage.getBody());
				if (dataMessage.isBack()) {
					// logger.info("SENDBACK-> DataLength:"+dataMessage.getBody().length);
					channelFrom.getKey().writeAndFlush(dataMessage2);
				} else {
					channelFrom.getValue().writeAndFlush(dataMessage2);
					// logger.info("SEND-> DataLength:"+dataMessage.getBody().length);
				}
			}
		} else {
			// 没找对应的channelPare，直接返回ResultMessage,子类型为Error，ExtendMsg为lSocketId
			ResultMessage resultMessage = new ResultMessage(Const.RTN_ERROR, dataMessage.getlSocketId() + "|channel error", false);
			ctx.channel().writeAndFlush(resultMessage);
			logger.error("can not found channelPare....Msg->" + dataMessage + "  lSocketId->" + dataMessage.getlSocketId());
		}
	}

}
