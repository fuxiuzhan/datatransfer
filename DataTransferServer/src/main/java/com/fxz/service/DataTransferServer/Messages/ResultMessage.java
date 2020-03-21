package com.fxz.service.DataTransferServer.Messages;

import java.nio.ByteBuffer;

import com.fxz.service.DataTransferServer.Const.Const;

public class ResultMessage extends BaseMessage {

	/**
	 * @Fields serialVersionUID :
	 *         使用ResultMessage对异常中断进行封装，有一点可以不改动报文类型，因为ResultMessage传输方向
	 *         是唯一的，既只可能从目标端传输给发送端，所以server端以一旦受到ResultMessage报文直接转发给ChannelPare
	 *         key 端
	 */
	private static final long serialVersionUID = 1L;
	private byte rsult_type = Const.RTN_VERIFY;
	private String extendMsg = "N/A";
	private boolean succ = false;
	private boolean back = false;

	public boolean isSucc() {
		return succ;
	}

	public boolean isBack() {
		return back;
	}

	public ResultMessage(byte type, String msg, boolean result, boolean isback) {
		super(Const.MSG_REULST);
		setExtendMsg(msg);
		succ = result;
		this.rsult_type = type;
		this.back = isback;
		ByteBuffer buffer = ByteBuffer.allocate(1 + 1 + 1 + msg.getBytes().length + 4);
		if (back) {
			buffer.put((byte) 0x01);
		} else {
			buffer.put((byte) 0x00);
		}
		if (result) {
			buffer.put((byte) 0x01);
		} else {
			buffer.put((byte) 0x00);
		}
		buffer.put(type);
		buffer.putInt(msg.getBytes().length);
		buffer.put(msg.getBytes());
		setBody(buffer.array());
	}

	public ResultMessage(byte type, String msg, boolean result) {
		super(Const.MSG_REULST);
		setExtendMsg(msg);
		succ = result;
		this.rsult_type = type;
		ByteBuffer buffer = ByteBuffer.allocate(1 + 1 + 1 + msg.getBytes().length + 4);
		if (back) {
			buffer.put((byte) 0x01);
		} else {
			buffer.put((byte) 0x00);
		}
		if (result) {
			buffer.put((byte) 0x01);
		} else {
			buffer.put((byte) 0x00);
		}
		buffer.put(type);
		buffer.putInt(msg.getBytes().length);
		buffer.put(msg.getBytes());
		setBody(buffer.array());
	}

	public ResultMessage(BaseMessage baseMessage) {
		super(Const.MSG_REULST);
		setSocketUUID(baseMessage.getSocketUUID());
		ByteBuffer buffer = ByteBuffer.wrap(baseMessage.getBody());
		if (buffer.get() == 0x01) {
			this.back = true;
		} else {
			this.back = false;
		}
		if (buffer.get() == 0x01) {
			this.succ = true;
		} else {
			this.succ = false;
		}
		this.rsult_type = buffer.get();
		int msglen = buffer.getInt();
		byte[] msgbytes = new byte[msglen];
		buffer.get(msgbytes);
		this.extendMsg = new String(msgbytes);
	}

	public byte getRsult_type() {
		return rsult_type;
	}

	public String getExtendMsg() {
		return extendMsg;
	}

	public void setExtendMsg(String extendMsg) {
		this.extendMsg = extendMsg;
	}

	@Override
	public String toString() {
		return "ResultMessage [rsult_type=" + rsult_type + ", extendMsg=" + extendMsg + ", succ=" + succ + "]";
	}
}
