package com.fxz.service.DataTransferServer.Messages.Handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import com.fxz.service.DataTransferServer.Const.Const;
import com.fxz.service.DataTransferServer.Const.Params;
import com.fxz.service.DataTransferServer.Messages.BaseMessage;
import com.fxz.service.DataTransferServer.Messages.ResultMessage;

/**
 * @ClassName: ResultHandler
 * @Description: 处理结果返回类型，主要使用在认证，连接建立，连接释放等地
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月12日 下午2:03:21
 */
public class ResultHandler implements IProcessMessage {

	Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws IOException {
		// TODO Auto-generated method stub
		ResultMessage resultMessage = new ResultMessage((BaseMessage) baseMessage);
		Map<Channel, Channel> channelPare = Params.getChannelPare(resultMessage.getSocketUUID());//
		switch (resultMessage.getRsult_type()) {
		case Const.RTN_CONNECT:
			ResultMessage resultMessage2 = new ResultMessage(resultMessage.getRsult_type(), resultMessage.getExtendMsg(), resultMessage.isSucc());
			resultMessage2.setSocketUUID(baseMessage.getSocketUUID());
			if (channelPare != null) {
				for (Entry<Channel, Channel> channelFrom : channelPare.entrySet()) {
					channelFrom.getKey().writeAndFlush(resultMessage2);
				}
			} else {
				logger.error("can not found channelPare....Msg->" + resultMessage);
			}
			break;
		case Const.RTN_VERIFY:
			// useless message
			break;
		case Const.RTN_ERROR:
			if (channelPare != null) {
				for (Entry<Channel, Channel> channelFrom : channelPare.entrySet()) {
					ResultMessage tresultMessage = new ResultMessage(resultMessage.getRsult_type(), resultMessage.getExtendMsg(), resultMessage.isSucc(), resultMessage.isBack());
					tresultMessage.setSocketUUID(baseMessage.getSocketUUID());
					if (resultMessage.isBack()) {
						// 发往目标端
						channelFrom.getValue().writeAndFlush(tresultMessage);
					} else {
						// 发往源端
						channelFrom.getKey().writeAndFlush(tresultMessage);
					}
				}
			} else {
				logger.error("can not found channelPare....Msg->" + resultMessage);
			}
			break;
		default:
			break;
		}
	}

}
