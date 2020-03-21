package com.fxz.service.DataTransferServer.Statistic;

import java.util.ArrayList;
import java.util.List;

import com.fxz.service.DataTransferServer.Auth.config.AuthConfig;

/**
 * @ClassName: ServerConfig
 * @Description: 服务端参数配置文件，配置文件包含以下几部分 1、网络接口配置 2、网络通信配置 3、客户端信息列表
 *               1、网络接口部分包含以下内容{1、绑定IP地址 2、绑定端口号} 2、网络通信配置包含以下内容{1、非对称加密算法
 *               2、对称加密算法 3、认证摘要 4、报文摘要 5、算法报文校验}
 *               3、客户端信息列表是List类型，包含以下内容{1、ClientId 2、UserId 3、AppId}
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月17日 上午7:46:31
 */
public class ServerConfig {
	/*
	 * 网络接口配置
	 */
	private static String bindIP = "0.0.0.0";
	private static int bindPort = 9000;
	/*
	 * 网络通信配置
	 */
	private static AuthConfig authConfig = new AuthConfig();

	/*
	 * 客户端列表配置
	 */
	private static List<User> userList = new ArrayList<>();

	public static String getBindIP() {
		return bindIP;
	}

	public static void setBindIP(String bindIP) {
		ServerConfig.bindIP = bindIP;
	}

	public static int getBindPort() {
		return bindPort;
	}

	public static void setBindPort(int bindPort) {
		ServerConfig.bindPort = bindPort;
	}

	public static AuthConfig getAuthConfig() {
		return authConfig;
	}

	public static void setAuthConfig(AuthConfig authConfig) {
		ServerConfig.authConfig = authConfig;
	}

	public static List<User> getUserList() {
		return userList;
	}

	public static void setUserList(List<User> userList) {
		ServerConfig.userList = userList;
	}

}
