package com.fxz.service.DataTransferServer.Codec;

import java.nio.ByteOrder;
import java.util.List;
import com.fxz.service.DataTransferServer.Messages.BaseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

/**
 * @ClassName: Message2BytesCodec
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午3:40:07
 */
public class Message2BytesCodec extends ByteToMessageCodec<BaseMessage> {

	/*
	 * |HEADER1|HEADER2|Version|SocketUUID|UserID|AppID|Type|DATA_Length|DATA|(non
	 * Javadoc)
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, BaseMessage msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		out.order(ByteOrder.LITTLE_ENDIAN);
		out.writeByte(BaseMessage.HEADER1);
		out.writeByte(BaseMessage.HEADER2);
		out.writeByte(msg.getVerion());
		/*
		 * 增加 SocketUUID，userID,appID
		 */
		out.writeInt(msg.getSocketUUID().getBytes().length);
		out.writeBytes(msg.getSocketUUID().getBytes());
		out.writeInt(msg.getUserID().getBytes().length);
		out.writeBytes(msg.getUserID().getBytes());
		out.writeInt(msg.getAppID().getBytes().length);
		out.writeBytes(msg.getAppID().getBytes());
		/*
		 * end
		 */
		out.writeInt(msg.getCheckSum().length);
		out.writeBytes(msg.getCheckSum());
		out.writeByte(msg.getMessageType());
		out.writeInt(msg.getBody().length);
		out.writeBytes(msg.getBody());
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		if (in.readableBytes() >= (1 + 1 + 1 + 4)) {// |HEADER1|HEADER2|VERSION|CHECKSUMLEN|CHECKSUM|TYPE|DATA_LENGTH|DATA
			in.markReaderIndex();
			byte Header1 = in.readByte();
			byte Header2 = in.readByte();
			if (Header1 == BaseMessage.HEADER1 && Header2 == BaseMessage.HEADER2) {
				BaseMessage baseMessage = new BaseMessage();
				baseMessage.setVerion(in.readByte());
				/*
				 * Start socketUUid,userId,appId
				 */
				int uuidlen = in.readInt();
				if (in.readableBytes() >= uuidlen + 4) {
					byte[] uuidbytes = new byte[uuidlen];
					in.readBytes(uuidbytes);
					baseMessage.setSocketUUID(new String(uuidbytes));
					int useridlen = in.readInt();
					if (in.readableBytes() >= useridlen + 4) {
						byte[] useridbytes = new byte[useridlen];
						in.readBytes(useridbytes);
						baseMessage.setUserID(new String(useridbytes));
						int appidlen = in.readInt();
						if (in.readableBytes() >= appidlen + 4) {
							byte[] appidbytes = new byte[appidlen];
							in.readBytes(appidbytes);
							baseMessage.setAppID(new String(appidbytes));
						} else {
							in.resetReaderIndex();
							return;
						}
					} else {
						in.resetReaderIndex();
						return;
					}
				} else {
					in.resetReaderIndex();
					return;
				}
				/*
				 * End
				 */
				int checksumlen = in.readInt();
				if (in.readableBytes() > checksumlen + 4) {
					byte[] checksum = new byte[checksumlen];
					in.readBytes(checksum);
					baseMessage.setCheckSum(checksum);
					baseMessage.setMessageType(in.readByte());
					int bufferlen = in.readInt();
					if (in.readableBytes() >= bufferlen) {
						byte[] buffer = new byte[bufferlen];
						in.readBytes(buffer);
						baseMessage.setBody(buffer);
						out.add(baseMessage);
					} else {
						in.resetReaderIndex();
					}
				} else {
					in.resetReaderIndex();
				}
			} else {
				in.resetReaderIndex();
			}
		}
	}

}
