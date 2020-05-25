package com.fxz.channelswitcher.datatransferserver.utils;

import java.io.Serializable;

/**
 * @ClassName: ClientInfo
 * @Description: 客户端主机信息，方便管理端对主机的管理，但主句管理不能使用主机名称，因为主机名有可能重复
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月12日 下午2:22:17
 */
public class ClientInfo implements Serializable {
    /**
     * @Fields serialVersionUID : TODO
     */
    private static final long serialVersionUID = 1L;
    private String clientID;
    private String hostName;
    private String hostIP;
    private String hostSysType;
    private long memSize;
    private boolean onLine;
    private String userID = "userid";
    private String appID = "appid";
    private String mac;
    private String endPoint;
    private boolean enableReplace;

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public void setEnableReplace(boolean enableReplace) {
		this.enableReplace = enableReplace;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public boolean isEnableReplace() {
		return enableReplace;
	}

	public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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

    public boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    public String getHostSysType() {
        return hostSysType;
    }

    public void setHostSysType(String hostSysType) {
        this.hostSysType = hostSysType;
    }

    public long getMemSize() {
        return memSize;
    }

    public void setMemSize(long memSize) {
        this.memSize = memSize;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @Override
    public String toString() {
        return "ClientInfo [clientID=" + clientID + ", hostName=" + hostName + ", hostIP=" + hostIP + ", hostSysType=" + hostSysType + ", memSize=" + memSize + ", onLine=" + onLine + ", userID="
                + userID + ", appID=" + appID + ", mac=" + mac + "]";
    }

}
