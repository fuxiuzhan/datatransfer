package com.fxz.channelswitcher.datatransferserver.config;


import com.fxz.channelswitcher.datatransferserver.auth.config.AuthConfig;
import com.fxz.channelswitcher.datatransferserver.statistic.User;

/**
 * @ClassName: LocalServerConfig
 * @Description: 客户端配置信息 客户端配置信息主要包含以下几部分1、服务端地址配置 2、客户端信息配置 3、通讯配置 4、本地接口配置
 *               5、断开重连配置
 * 
 *               1、服务端地址配置{1、服务端域名/IP，2、服务端Port} 2、客户端信息配置{1、ClientId，2、UserId
 *               3、AppId} 3、通讯配置 4、本地接口配置（主要值连接对端端口使用的地址） 5、断开重连{1、重连间隔}
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月17日 上午8:06:57
 */
public class LocalServerConfig {
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
	 * 绑定本地端口
	 */
	private static String bindIP = "0.0.0.0";
	/*
	 * 断开重连间隔
	 */
	private static int reTryInternal = 60 * 1000;

	public static String getServerIP() {
		return serverIP;
	}

	public static void setServerIP(String serverIP) {
		LocalServerConfig.serverIP = serverIP;
	}

	public static int getServerPort() {
		return serverPort;
	}

	public static void setServerPort(int serverPort) {
		LocalServerConfig.serverPort = serverPort;
	}

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		LocalServerConfig.user = user;
	}

	public static AuthConfig getAuthConfig() {
		return authConfig;
	}

	public static void setAuthConfig(AuthConfig authConfig) {
		LocalServerConfig.authConfig = authConfig;
	}

	public static String getBindIP() {
		return bindIP;
	}

	public static void setBindIP(String bindIP) {
		LocalServerConfig.bindIP = bindIP;
	}

	public static int getReTryInternal() {
		return reTryInternal;
	}

	public static void setReTryInternal(int reTryInternal) {
		LocalServerConfig.reTryInternal = reTryInternal;
	}
}
