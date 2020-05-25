package com.fxz.channelswitcher.datatransferserver.messages;

import com.fxz.channelswitcher.datatransferserver.constant.Const;

import java.nio.ByteBuffer;
import java.util.UUID;


/**
 * @ClassName: ConnectMessage
 * @Description: 实现请求端连接目标端端口，连接成功会产生ChannelPare（根据SocketUUID区分，在Server端都是通道复用）
 *               考虑到连接第一次建立时第一次数据的发送是在发起方还是接收方
 *               ，所以无论是在发起方和接受方应该对链路进行唯一标识，不然会出现双方不一致的情况
 * 
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月12日 下午3:06:11
 */
public class ConnectMessage extends BaseMessage {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private int port;
	private String ip = "127.0.0.1";
	private String clientId = "clientId";
	private String lSocketId = UUID.randomUUID().toString();

	public int getPort() {
		return port;
	}

	public void setlSocketId(String lSocketId) {
		this.lSocketId = lSocketId;
	}

	public String getlSocketId() {
		return lSocketId;
	}

	public String getIp() {
		return ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientId() {
		return clientId;
	}

	public ConnectMessage(BaseMessage baseMessage) {
		super(Const.MSG_CONNECT);
		setSocketUUID(baseMessage.getSocketUUID());
		setAppID(baseMessage.getAppID());
		setUserID(baseMessage.getUserID());
		ByteBuffer buffer = ByteBuffer.wrap(baseMessage.getBody());
		int len = buffer.getInt();
		byte[] arry = new byte[len];
		buffer.get(arry);
		this.lSocketId = new String(arry);
		len = buffer.getInt();
		arry = new byte[len];
		buffer.get(arry);
		this.ip = new String(arry);
		len = buffer.getInt();
		arry = new byte[len];
		buffer.get(arry);
		this.port = bytes2Int(arry);
		len = buffer.getInt();
		arry = new byte[len];
		buffer.get(arry);
		this.clientId = new String(arry);
	}

	public ConnectMessage(String ip, int port, String socketuuid, String clientid, String lsocketid) {
		super(Const.MSG_CONNECT);
		this.lSocketId = lsocketid;
		this.ip = ip;
		this.clientId = clientid;
		setSocketUUID(socketuuid);
		this.port = port;
		// ip|port|clientId
		ByteBuffer buffer = ByteBuffer.allocate(4 + lSocketId.getBytes().length + ip.getBytes().length + 4 + int2Bytes(port).length + 4 + clientid.getBytes().length + 4);
		buffer.putInt(lSocketId.getBytes().length);
		buffer.put(lSocketId.getBytes());
		buffer.putInt(ip.getBytes().length);
		buffer.put(ip.getBytes());
		buffer.putInt(int2Bytes(port).length);
		buffer.put(int2Bytes(port));
		buffer.putInt(clientid.getBytes().length);
		buffer.put(clientid.getBytes());
		setBody(buffer.array());
	}

	public ConnectMessage(String ip, int port, String socketuuid, String clientid) {
		super(Const.MSG_CONNECT);
		this.ip = ip;
		this.clientId = clientid;
		setSocketUUID(socketuuid);
		this.port = port;
		// ip|port|clientId
		ByteBuffer buffer = ByteBuffer.allocate(4 + lSocketId.getBytes().length + ip.getBytes().length + 4 + int2Bytes(port).length + 4 + clientid.getBytes().length + 4);
		buffer.putInt(lSocketId.getBytes().length);
		buffer.put(lSocketId.getBytes());
		buffer.putInt(ip.getBytes().length);
		buffer.put(ip.getBytes());
		buffer.putInt(int2Bytes(port).length);
		buffer.put(int2Bytes(port));
		buffer.putInt(clientid.getBytes().length);
		buffer.put(clientid.getBytes());
		setBody(buffer.array());
	}

	private int bytes2Int(byte[] bytes) {
		return bytes[3] & 0xFF | (bytes[2] & 0xFF) << 8 | (bytes[1] & 0xFF) << 16 | (bytes[0] & 0xFF) << 24;
	}

	private byte[] int2Bytes(int num) {
		return new byte[] { (byte) ((num >> 24) & 0xFF), (byte) ((num >> 16) & 0xFF), (byte) ((num >> 8) & 0xFF), (byte) (num & 0xFF) };
	}

	@Override
	public String toString() {
		return "ConnectMessage [port=" + port + ", ip=" + ip + ", clientId=" + clientId + ", getSocketUUID()=" + getSocketUUID() + " ,BodyLength=" + getBody().length + "]";
	}

}
