package com.fxz.channelswitcher.datatransferserver.statistic;

import com.fxz.channelswitcher.datatransferserver.auth.config.AuthConfig;
import com.fxz.channelswitcher.datatransferserver.utils.FileUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: InitConfig
 * @Description:负责读取三种客户端的配置文件
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月17日 下午2:52:31
 */
public class InitConfig {
    static Logger logger = Logger.getLogger(InitConfig.class);

    public static boolean readServerConfig(String filepath) {
        try {
            SAXReader reader = new SAXReader();
            Document documentt = reader.read(new File(filepath));
            Element root = documentt.getRootElement();
            /*
             * 读取NetConfig
             */
            Element netconfig = root.element("NetConfig");
            String serverIP = netconfig.element("IP").getText();
            logger.info("ServerIP->" + serverIP);
            ServerConfig.setBindIP(serverIP);
            int serverPort = Integer.parseInt(netconfig.element("PORT").getText());
            logger.info("ServerPort->" + serverPort);
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
                try {
                    user.setForceConnect(client.element("EnableReplace").getText().equalsIgnoreCase("true"));
                } catch (Exception e) {
                }
                userList.add(user);
                logger.info("AddUser->" + user);
            }
            ServerConfig.setUserList(userList);
            return true;
        } catch (Exception e) {
            logger.error("Error->" + e);
            return false;
        }
    }

    public static boolean readLocalServerConfig(String filepad) {
        try {
            logger.info("start reading config file->" + filepad);
            SAXReader reader = new SAXReader();
            Document documentt = reader.read(new File(filepad));
            Element root = documentt.getRootElement();
            /*
             * 读取NetConfig
             */
            Element netconfig = root.element("NetConfig");
            String serverIP = netconfig.element("ServerIP").getText();
            logger.info("ServerIP->" + serverIP);
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

            /*
             * 读取客户端信息
             */
            Element userconfig = root.element("ClientInfo");
            User user = new User();
            user.setClientId(userconfig.element("ClientId").getText());
            user.setUserId(userconfig.element("UserId").getText());
            user.setAppId(userconfig.element("AppId").getText());
            LocalServerConfig.setUser(user);
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
            return true;
        } catch (Exception e) {
            logger.error("Error->" + e);
            return false;
        }
    }

    public static boolean readClientConfig(String filepath) {
        try {
            logger.info("start read clientconfig file->" + filepath);
            SAXReader reader = new SAXReader();
            Document documentt = reader.read(new File(filepath));
            Element root = documentt.getRootElement();
            /*
             * 读取NetConfig
             */
            Element netconfig = root.element("NetConfig");
            String serverIP = netconfig.element("ServerIP").getText();
            logger.info("ServerIP->" + serverIP);
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
            /*
             * 读取客户端信息
             */
            Element userconfig = root.element("ClientInfo");
            User user = new User();
            user.setClientId(userconfig.element("ClientId").getText());
            user.setUserId(userconfig.element("UserId").getText());
            user.setAppId(userconfig.element("AppId").getText());
            ClientConfig.setUser(user);
            /*
             * 读取端口映射
             */
            List<Element> portlist = root.element("PortList").elements("TargetClient");
            List<String> portlist2 = new ArrayList<>();
            for (Element element : portlist) {
                String client_id = element.attributeValue("Id");
                String local_port = element.element("LocalPort").getText();
                String remote_port = element.element("RemotePort").getText();
                String remote_ip = null;
                ;
                try {
                    remote_ip = element.element("RemoteIP").getText();
                } catch (Exception e) {
                    logger.error("RemoteIP xmlnode nout found useing default 127.0.0.1...");
                }
                if (remote_ip == null || remote_ip.length() == 0) {
                    remote_ip = "127.0.0.1";
                }
                portlist2.add(client_id + ":" + local_port + ":" + remote_port + ":" + remote_ip);
            }
            ClientConfig.setPortList(portlist2);

            /*
             * 读取断线重连间隔
             */
            Element maxinternal = root.element("MaxRetryInternal");
            try {
                Element maxTryTimes = root.element("MaxTryTimes");
                ClientConfig.setMaxTryTimes(Integer.parseInt(maxTryTimes.getText()));
            } catch (Exception e) {

            }
            ClientConfig.setReTryInternal(Integer.parseInt(maxinternal.getText()));
            return true;
        } catch (Exception e) {
            logger.error("Error->" + e);
            return false;
        }
    }

    public static void initIpFilter() {
        ClientConfig.getWhiteSet().addAll(FileUtils.readFile(ClientConfig.WHITE_LIST));
        List<String> blackIpList = new ArrayList<>();
        blackIpList.addAll(FileUtils.readFile(ClientConfig.AUTO_DETECT_LIST));
        blackIpList.addAll(FileUtils.readFile(ClientConfig.BLACK_LIST));
        if (blackIpList.size() > 0) {
            for (String ip : blackIpList) {
            	  if (ip!=null && ip.length()>0) {
                    ClientConfig.addBlackMap(ip);
                }
            }
        }
    }
}
