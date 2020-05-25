package com.fxz.channelswitcher.datatransferserver.utils;

import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: Params
 * @Description: 保存常量和连接Hash表
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月12日 下午10:21:10
 */
public class ClientParams {
	private static String userId = "userId";
	private static String appId = "appId";
	private static Channel mainChannel;
	
	public static Channel getMainChannel() {
		return mainChannel;
	}

	public static void setMainChannel(Channel mainChannel) {
		ClientParams.mainChannel = mainChannel;
	}

	public static String getUserId() {
		return userId;
	}

	public static void setUserId(String userId) {
		ClientParams.userId = userId;
	}

	public static String getAppId() {
		return appId;
	}

	public static void setAppId(String appId) {
		ClientParams.appId = appId;
	}

	private static Map<String, Channel> clientMap = new HashMap<>();

	public static Channel getChannel(String socketUUid) {
		return clientMap.get(socketUUid);
	}

	public static void addChannel(String socketuuid, Channel channel) {
		clientMap.put(socketuuid, channel);
	}

	public static void removeChannel(String socketuuid)
	{
		clientMap.remove(socketuuid);
	}
}
