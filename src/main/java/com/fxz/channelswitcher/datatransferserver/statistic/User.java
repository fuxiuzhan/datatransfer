package com.fxz.channelswitcher.datatransferserver.statistic;

/**
 * @ClassName: User
 * @Description: 客户端，主要包括以下内容 1、ClientId 2、UserId 3、AppId
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月17日 上午7:51:16
 */
public class User {
    private String clientId;
    private String userId;
    private String appId;
    private boolean forceConnect;


    public void setForceConnect(boolean forceConnect) {
        this.forceConnect = forceConnect;
    }


    public boolean isForceConnect() {
        return forceConnect;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "User{" +
                "clientId='" + clientId + '\'' +
                ", userId='" + userId + '\'' +
                ", appId='" + appId + '\'' +
                ", forceConnect=" + forceConnect +
                '}';
    }
}
