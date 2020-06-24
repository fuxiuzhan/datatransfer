package com.fxz.channelswitcher.datatransferserver.messages;

import com.fxz.channelswitcher.datatransferserver.constant.Const;

import java.nio.ByteBuffer;
import java.util.Arrays;


/**
 * @ClassName: DataMessage
 * @Description: 数据封装报文，也是整个结构与底层RawSocket交互报文。为了实现底层RawSocketChannel的区分，
 *               增加lSocketId字段来区分RawSocket
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月14日 下午9:29:06
 */
public class DataMessage extends BaseMessage {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String lSocketId = "n/a";
	private boolean back = false;

	public String getlSocketId() {
		return lSocketId;
	}

	public boolean isBack() {
		return back;
	}

	public DataMessage(BaseMessage baseMessage) {
		super(Const.MSG_DATA);
		setUserID(baseMessage.getUserID());
		ByteBuffer buffer = ByteBuffer.wrap(baseMessage.getBody());
		if (buffer.get() == 0x01) {
			back = true;
		} else {
			back = false;
		}
		int idlen = buffer.getInt();
		byte[] idarray = new byte[idlen];
		buffer.get(idarray);
		this.lSocketId = new String(idarray);
		int bodylen = buffer.getInt();
		byte[] body = new byte[bodylen];
		buffer.get(body);
		setBody(body);
		setSocketUUID(baseMessage.getSocketUUID());
		setAppID(baseMessage.getAppID());
	}

	public DataMessage(String socketuuid, String lscoketid, String userid, String appid, byte[] buffer, boolean isback) {
		super(Const.MSG_DATA);
		this.lSocketId = lscoketid;
		this.back = isback;
		setUserID(userid);
		ByteBuffer bytebuffer = ByteBuffer.allocate(1 + 4 + lSocketId.getBytes().length + 4 + buffer.length);
		if (back) {
			bytebuffer.put((byte) 0x01);
		} else {
			bytebuffer.put((byte) 0x00);
		}
		bytebuffer.putInt(lSocketId.getBytes().length);
		bytebuffer.put(lSocketId.getBytes());
		bytebuffer.putInt(buffer.length);
		bytebuffer.put(buffer);
		setBody(bytebuffer.array());
		setSocketUUID(socketuuid);
		setAppID(appid);
	}

	public DataMessage(String socketuuid, String lscoketid, String userid, String appid, byte[] buffer) {
		super(Const.MSG_DATA);
		this.lSocketId = lscoketid;
		setUserID(userid);
		ByteBuffer bytebuffer = ByteBuffer.allocate(1 + 4 + lSocketId.getBytes().length + 4 + buffer.length);
		if (back) {
			bytebuffer.put((byte) 0x01);
		} else {
			bytebuffer.put((byte) 0x00);
		}
		bytebuffer.putInt(lSocketId.getBytes().length);
		bytebuffer.put(lSocketId.getBytes());
		bytebuffer.putInt(buffer.length);
		bytebuffer.put(buffer);
		setBody(bytebuffer.array());
		setSocketUUID(socketuuid);
		setAppID(appid);
	}

	@Override
	public String toString() {
		return "DataMessage [lSocketId=" + lSocketId + ", back=" + back + "]";
	}
	
}
