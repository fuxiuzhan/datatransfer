package com.fxz.service.DataTransferServer.Ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.fxz.service.DataTransferServer.Const.Params;
import com.fxz.service.DataTransferServer.Statistic.User;
import com.fxz.service.DataTransferServer.Utils.ClientInfo;

/**
 * @ClassName: UserCtrl
 * @Description: 主要实现用户查询
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月12日 下午7:31:28
 */
public class UserCtrl {
	static Logger logger = Logger.getLogger(UserCtrl.class);

	public static boolean verifyUser(String userId, String appId) {
		logger.info("verifyUser->userId:" + userId + " appId:" + appId);
		if (userMap.containsKey(appId) && userMap.get(appId).equalsIgnoreCase(userId)) {
			return true;
		}
		return false;
	}
	private static Map<String, String> userMap = new HashMap<String, String>();

	public static void addUser(User user) {
		userMap.put(user.getAppId(), user.getUserId());
		ClientInfo clientinfo = new ClientInfo();
		clientinfo.setAppID(user.getAppId());
		clientinfo.setClientID(user.getClientId());
		clientinfo.setUserID(user.getUserId());
		clientinfo.setEnableReplace(user.isForceConnect());
		Params.addClientList(clientinfo);
	}

	public static void addUserList(List<User> userlist) {
		for (User user : userlist) {
			addUser(user);
		}
	}
}
