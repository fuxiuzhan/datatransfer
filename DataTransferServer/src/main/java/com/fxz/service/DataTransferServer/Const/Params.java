package com.fxz.service.DataTransferServer.Const;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

import com.fxz.service.DataTransferServer.Utils.ClientInfo;

/**
 * @ClassName: Params
 * @Description: 静态变量的集中引用，包含服务端维护的客户端列表，SocketUUID标识，
 * 通道的配对关系，客户端是否经过认证，节点信息的添加与移除。着重说明一下在Socket出现异常
 * 时的处理逻辑。配对通道有一方出现连接断开就主动将正常连接的客户端也断开，名返回Result报文
 * 说明原因，并更新在线客户端信息，而客户端具备重连机制，在异常断线后会自动重连。在测试版本中
 * 异常断开则直接断开上层连接，在后续版本中将加入重连与会话恢复操作
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月12日 下午2:13:10
 */
public class Params {
    /*
     * clientMap的数据结构为：<SocketUUID,<SocketFrom,SocketTo>>
     */
    private static Map<String, Map<Channel, Channel>> clientMap = new HashMap<>();
    private static Map<String, Channel> liveClients = new HashMap<>();

    public static Map<Channel, Channel> getChannelPare(String socketUUID) {
        return clientMap.get(socketUUID);
    }

    public static void addOnLineClient(String clientId, Channel ctx) {
        liveClients.put(clientId, ctx);
    }

    public static Channel getOnlineClient(String clientId) {
        return liveClients.get(clientId);
    }

    public static void removeOnlineClient(String clientId) {
        liveClients.remove(clientId);
    }

    public static void addChannelPare(String socketUUID, Map<Channel, Channel> channelPare) {
        clientMap.put(socketUUID, channelPare);
    }

    /*
     * 在线客户端列表,存储所有客户端信息，具体的信息会在对应的ClientInfo中存储，能够存在于此表中也代表客户端是合法的。
     */
    private static Map<String, ClientInfo> clientList = new HashMap<>();

    public static void addClientList(ClientInfo clientInfo) {
        clientList.put(clientInfo.getClientID(), clientInfo);
    }

    public static ClientInfo getClientInfo(String clientID) {
        return clientList.get(clientID);
    }

    public static boolean clientVeriyed(String clientid, String userid, String appid) {
        ClientInfo clientinfo = clientList.get(clientid);
        if (clientinfo == null) {
            return false;
        }
        if (clientinfo.getAppID().equalsIgnoreCase(appid) && clientinfo.getUserID().equalsIgnoreCase(userid)) {
            return true;
        }
        return false;
    }
}
