package com.fxz.channelswitcher.datatransferserver.config;

import com.fxz.channelswitcher.datatransferserver.auth.config.AuthConfig;
import com.fxz.channelswitcher.datatransferserver.statistic.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ClientConfig
 * @Description: 客户端配置信息 客户端配置信息主要包含以下几部分1、服务端地址配置 2、客户端信息配置 3、通讯配置 4、端口映射列表
 *               5、断开重连配置
 * 
 *               1、服务端地址配置{1、服务端域名/IP，2、服务端Port} 2、客户端信息配置{1、ClientId，2、UserId
 *               3、AppId} 3、通讯配置 4、端口映射List<LocalPort:RemotePort> 5、断开重连{1、重连间隔}
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月17日 上午7:57:27
 */
public class ClientConfig {
	/*
	 * 服务端地址配置
	 */
	private static String serverIP = "127.0.0.1";
	private static int serverPort = 9000;
	/*
	 * 客户端信息配置
	 */
	private static User user = new User();
	/*
	 * 通讯配置
	 */
	private static AuthConfig authConfig = new AuthConfig();
	/*
	 * 端口映射
	 */
	private static List<String> portList = new ArrayList<>();
	/*
	 * 断开重连间隔
	 */
	private static int reTryInternal = 60 * 1000;

	public static String getServerIP() {
		return serverIP;
	}

	public static void setServerIP(String serverIP) {
		ClientConfig.serverIP = serverIP;
	}

	public static int getServerPort() {
		return serverPort;
	}

	public static void setServerPort(int serverPort) {
		ClientConfig.serverPort = serverPort;
	}

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		ClientConfig.user = user;
	}

	public static AuthConfig getAuthConfig() {
		return authConfig;
	}

	public static void setAuthConfig(AuthConfig authConfig) {
		ClientConfig.authConfig = authConfig;
	}

	public static List<String> getPortList() {
		return portList;
	}

	public static void setPortList(List<String> portList) {
		ClientConfig.portList = portList;
	}

	public static int getReTryInternal() {
		return reTryInternal;
	}

	public static void setReTryInternal(int reTryInternal) {
		ClientConfig.reTryInternal = reTryInternal;
	}


}
