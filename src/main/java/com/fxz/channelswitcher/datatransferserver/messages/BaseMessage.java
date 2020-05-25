package com.fxz.channelswitcher.datatransferserver.messages;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @ClassName: BaseMessage
 * @Description: 基本报文
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午4:29:09
 */
public class BaseMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final byte HEADER1 = 0x0f;
	public static final byte HEADER2 = 0x0f;
	private String socketUUID = "uuid";// 连接ID，对端点系统的多连接进行区分
	private String userID = "userid";// 用户ID标识，用于用户识别
	private String appID = "appid";// 用户认证标识，用户认证用户是否合法
	private byte messageType = 0x00;// 报文类型
	private byte verion = 0x00;// 报文版本
	private byte[] body = new byte[] { 0x30, 0x31 };// 数据部分
	private byte[] checkSum = new byte[] { 0x30, 0x31 };// 数据报文部分校验

	public BaseMessage() {
	}

	public BaseMessage(BaseMessage bmessage) {
		this.setAppID(bmessage.getAppID());
		this.setSocketUUID(bmessage.getSocketUUID());
		this.setUserID(bmessage.getUserID());
		this.setVerion(bmessage.getVerion());
		this.setMessageType(bmessage.getMessageType());
		this.setBody(bmessage.getBody());
		this.setCheckSum(bmessage.getCheckSum());
	}

	public BaseMessage(byte type) {
		this.setMessageType(type);
	}

	public BaseMessage(byte type, byte[] body) {
		this.messageType = type;
		this.body = body;
	}

	public BaseMessage(byte type, byte[] body, String socketuuid, String userid, String appid) {
		this.messageType = type;
		this.body = body;
		this.socketUUID = socketuuid;
		this.userID = userid;
		this.appID = appid;
	}

	public String getSocketUUID() {
		return socketUUID;
	}

	public void setSocketUUID(String socketUUID) {
		this.socketUUID = socketUUID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public byte getMessageType() {
		return messageType;
	}

	public void setMessageType(byte messageType) {
		this.messageType = messageType;
	}

	public byte getVerion() {
		return verion;
	}

	public void setVerion(byte verion) {
		this.verion = verion;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public byte[] getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(byte[] checkSum) {
		this.checkSum = checkSum;
	}

	@Override
	public String toString() {
		return "BaseMessage [socketUUID=" + socketUUID + ", userID=" + userID + ", appID=" + appID + ", messageType=" + messageType + ", verion=" + verion + ", body=" + body.length + ", checkSum="
				+ Arrays.toString(checkSum) + "]";
	}

}
