package com.fxz.service.DataTransferServer.Statistic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import com.fxz.service.DataTransferServer.Auth.config.AuthConfig;

/**
 * @ClassName: XmlConfig
 * @Description: 测试生成和读取三种配置文件测试
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月17日 上午8:12:50
 */
public class XmlConfig {
	public static void main(String[] args) throws IOException, DocumentException {
		new XmlConfig().createClientConfig();
	}

	private void createServerConfig() throws IOException, DocumentException {
		File file = new File("config/ServerConfig.xml");
		if (!file.exists()) {
			Document document = DocumentHelper.createDocument();
			Element rootConfig = document.addElement("ServerConfig");
			Element netConfig = rootConfig.addElement("NetConfig");
			/*
			 * 增加网络接口配置信息
			 */
			Element ip = netConfig.addElement("IP");
			ip.setText("127.0.0.1");
			Element port = netConfig.addElement("PORT");
			port.setText("9000");
			Element authConfig = rootConfig.addElement("AuthConfig");
			/*
			 * 配置认证信息
			 */
			Element nonsym = authConfig.addElement("NonSym");
			nonsym.setText("none");
			Element sym = authConfig.addElement("Sym");
			sym.setText("none");
			Element authdigest = authConfig.addElement("AuthDigest");
			authdigest.setText("none");
			Element messageDigest = authConfig.addElement("MessageDigest");
			messageDigest.setText("none");
			Element clientListConfig = rootConfig.addElement("ClientsList");
			/*
			 * 设置客户端信息列表
			 */
			for (int i = 0; i < 2; i++) {
				Element clients = clientListConfig.addElement("Client").addAttribute("ClientId", "ClientId-" + i);
				Element userId = clients.addElement("UserId");
				userId.setText("user_id_fxz_" + i);
				Element appId = clients.addElement("AppId");
				appId.setText("app_id_fxz_" + i);
			}
			OutputFormat format = new OutputFormat("", true);
			format.setLineSeparator("\r\n");
			format.setIndent(true);
			format.setNewlines(true);
			XMLWriter writer = new XMLWriter(new FileOutputStream("config/ServerConfig.xml"), format);
			writer.write(document);
			writer.close();
		}
		/*
		 * 
		 * 读取测试
		 */
		System.out.println("Reading....");
		SAXReader reader = new SAXReader();
		Document documentt = reader.read(new File("config/ServerConfig.xml"));
		Element root = documentt.getRootElement();
		/*
		 * 读取NetConfig
		 */
		Element netconfig = root.element("NetConfig");
		String serverIP = netconfig.element("IP").getText();
		System.out.println("ServerIP->" + serverIP);
		ServerConfig.setBindIP(serverIP);
		int serverPort = Integer.parseInt(netconfig.element("PORT").getText());
		System.out.println("ServerPort->" + serverPort);
		/*
		 * 读取通讯参数配置
		 */
		Element authconfig = root.element("AuthConfig");
		AuthConfig authConfig2 = new AuthConfig();
		authConfig2.setNonSymEncryt(authconfig.element("NonSym").getText());
		authConfig2.setSymEncrypt(authconfig.element("Sym").getText());
		authConfig2.setAuthDigest(authconfig.element("AuthDigest").getText());
		authConfig2.setMessageDigest(authconfig.element("MessageDigest").getText());
		ServerConfig.setAuthConfig(authConfig2);
		System.out.println(authConfig2);
		/*
		 * 读取客户端列表
		 */
		Element clientsconfig = root.element("ClientsList");
		List<Element> clientsList = clientsconfig.elements("Client");
		List<User> userList = new ArrayList<>();
		for (Element client : clientsList) {
			User user = new User();
			user.setClientId(client.attributeValue("ClientId"));
			user.setUserId(client.element("UserId").getText());
			user.setAppId(client.element("AppId").getText());
			userList.add(user);
			System.out.println(user);
		}
		ServerConfig.setUserList(userList);
	}

	private void createClientConfig() throws IOException, DocumentException {
		File file = new File("config/ClientConfig.xml");
		if (!file.exists()) {
			Document document = DocumentHelper.createDocument();
			Element rootConfig = document.addElement("ServerConfig");
			Element netConfig = rootConfig.addElement("NetConfig");
			/*
			 * 增加网络接口配置信息
			 */
			Element ip = netConfig.addElement("ServerIP");
			ip.setText("127.0.0.1");
			Element port = netConfig.addElement("ServerPort");
			port.setText("9000");
			Element authConfig = rootConfig.addElement("AuthConfig");
			/*
			 * 配置认证信息
			 */
			Element nonsym = authConfig.addElement("NonSym");
			nonsym.setText("none");
			Element sym = authConfig.addElement("Sym");
			sym.setText("none");
			Element authdigest = authConfig.addElement("AuthDigest");
			authdigest.setText("none");
			Element messageDigest = authConfig.addElement("MessageDigest");
			messageDigest.setText("none");
			/*
			 * 设置客户端信息
			 */
			Element clientconfig = rootConfig.addElement("ClientInfo");
			Element clientId = clientconfig.addElement("ClientId");
			clientId.setText("fxz_client_id");
			Element userId = clientconfig.addElement("UserId");
			userId.setText("fxz_client_id");
			Element appId = clientconfig.addElement("AppId");
			appId.setText("fxz_client_id");
			/*
			 * 设置端口映射列表
			 */
			Element clients = rootConfig.addElement("PortList");
			for (int i = 0; i < 2; i++) {
				Element tarcient = clients.addElement("TargetClient").addAttribute("Id", "Client-" + i);
				Element localp = tarcient.addElement("LocalPort");
				localp.setText("100" + i);
				Element tarp = tarcient.addElement("RemotePort");
				tarp.setText("100" + i);
			}

			/*
			 * 设置断开重连时间间隔
			 */

			Element maxinteral = rootConfig.addElement("MaxRetryInternal");
			maxinteral.setText("60000");
			OutputFormat format = new OutputFormat("", true);
			format.setLineSeparator("\r\n");
			format.setIndent(true);
			format.setNewlines(true);
			XMLWriter writer = new XMLWriter(new FileOutputStream("config/ClientConfig.xml"), format);
			writer.write(document);
			writer.close();
		}
		/*
		 * 读取客户端配置文件测试
		 */
		System.out.println("Start Reading....");
		SAXReader reader = new SAXReader();
		Document documentt = reader.read(new File("config/ClientConfig.xml"));
		Element root = documentt.getRootElement();
		/*
		 * 读取NetConfig
		 */
		Element netconfig = root.element("NetConfig");
		String serverIP = netconfig.element("ServerIP").getText();
		System.out.println("ServerIP->" + serverIP);
		ClientConfig.setServerIP(serverIP);
		/*
		 * 读取通讯参数配置
		 */
		Element authconfig = root.element("AuthConfig");
		AuthConfig authConfig2 = new AuthConfig();
		authConfig2.setNonSymEncryt(authconfig.element("NonSym").getText());
		authConfig2.setSymEncrypt(authconfig.element("Sym").getText());
		authConfig2.setAuthDigest(authconfig.element("AuthDigest").getText());
		authConfig2.setMessageDigest(authconfig.element("MessageDigest").getText());
		ClientConfig.setAuthConfig(authConfig2);
		System.out.println(authConfig2);

		/*
		 * 读取客户端信息
		 */
		Element userconfig = root.element("ClientInfo");
		User user = new User();
		user.setClientId(userconfig.element("ClientId").getText());
		user.setUserId(userconfig.element("UserId").getText());
		user.setAppId(userconfig.element("AppId").getText());
		ClientConfig.setUser(user);
		System.out.println(user);
		/*
		 * 读取端口映射
		 */
		List<Element> portlist = root.element("PortList").elements("TargetClient");
		List<String> portlist2 = new ArrayList<>();
		for (Element element : portlist) {
			String client_id = element.attributeValue("Id");
			String local_port = element.element("LocalPort").getText();
			String remote_port = element.element("RemotePort").getText();
			portlist2.add(client_id + ":" + local_port + ":" + remote_port);
		}
		ClientConfig.setPortList(portlist2);
		System.out.println(portlist2);

		/*
		 * 读取断线重连间隔
		 */
		Element maxinternal = root.element("MaxRetryInternal");
		ClientConfig.setReTryInternal(Integer.parseInt(maxinternal.getText()));

	}

	private void createLocalServerConfig() throws DocumentException, IOException {

		File file = new File("config/LcoalServerConfig.xml");
		if (!file.exists()) {
			Document document = DocumentHelper.createDocument();
			Element rootConfig = document.addElement("ServerConfig");
			Element netConfig = rootConfig.addElement("NetConfig");
			/*
			 * 增加网络接口配置信息
			 */
			Element ip = netConfig.addElement("ServerIP");
			ip.setText("127.0.0.1");
			Element port = netConfig.addElement("ServerPort");
			port.setText("9000");
			Element authConfig = rootConfig.addElement("AuthConfig");
			/*
			 * 配置认证信息
			 */
			Element nonsym = authConfig.addElement("NonSym");
			nonsym.setText("none");
			Element sym = authConfig.addElement("Sym");
			sym.setText("none");
			Element authdigest = authConfig.addElement("AuthDigest");
			authdigest.setText("none");
			Element messageDigest = authConfig.addElement("MessageDigest");
			messageDigest.setText("none");
			/*
			 * 设置客户端信息
			 */
			Element clientconfig = rootConfig.addElement("ClientInfo");
			Element clientId = clientconfig.addElement("ClientId");
			clientId.setText("fxz_client_id");
			Element userId = clientconfig.addElement("UserId");
			userId.setText("fxz_client_id");
			Element appId = clientconfig.addElement("AppId");
			appId.setText("fxz_client_id");
			/*
			 * 设置绑定IP
			 */
			Element bindip = rootConfig.addElement("BindIP");
			bindip.setText("127.0.0.1");

			/*
			 * 设置断开重连时间间隔
			 */

			Element maxinteral = rootConfig.addElement("MaxRetryInternal");
			maxinteral.setText("60000");
			OutputFormat format = new OutputFormat("", true);
			format.setLineSeparator("\r\n");
			format.setIndent(true);
			format.setNewlines(true);
			XMLWriter writer = new XMLWriter(new FileOutputStream("config/LocalServerConfig.xml"), format);
			writer.write(document);
			writer.close();
		}
		/*
		 * 读取客户端配置文件测试
		 */
		System.out.println("Start Reading....");
		SAXReader reader = new SAXReader();
		Document documentt = reader.read(new File("config/LocalServerConfig.xml"));
		Element root = documentt.getRootElement();
		/*
		 * 读取NetConfig
		 */
		Element netconfig = root.element("NetConfig");
		String serverIP = netconfig.element("ServerIP").getText();
		System.out.println("ServerIP->" + serverIP);
		LocalServerConfig.setServerIP(serverIP);
		LocalServerConfig.setServerPort(Integer.parseInt(netconfig.element("ServerPort").getText()));
		/*
		 * 读取通讯参数配置
		 */
		Element authconfig = root.element("AuthConfig");
		AuthConfig authConfig2 = new AuthConfig();
		authConfig2.setNonSymEncryt(authconfig.element("NonSym").getText());
		authConfig2.setSymEncrypt(authconfig.element("Sym").getText());
		authConfig2.setAuthDigest(authconfig.element("AuthDigest").getText());
		authConfig2.setMessageDigest(authconfig.element("MessageDigest").getText());
		LocalServerConfig.setAuthConfig(authConfig2);
		System.out.println(authConfig2);

		/*
		 * 读取客户端信息
		 */
		Element userconfig = root.element("ClientInfo");
		User user = new User();
		user.setClientId(userconfig.element("ClientId").getText());
		user.setUserId(userconfig.element("UserId").getText());
		user.setAppId(userconfig.element("AppId").getText());
		LocalServerConfig.setUser(user);
		System.out.println(user);
		/*
		 * 读取绑定IP
		 */
		Element bind_ip = root.element("BindIP");
		LocalServerConfig.setBindIP(bind_ip.getText());

		/*
		 * 读取断线重连间隔
		 */
		Element maxinternal = root.element("MaxRetryInternal");
		LocalServerConfig.setReTryInternal(Integer.parseInt(maxinternal.getText()));

	}
}
