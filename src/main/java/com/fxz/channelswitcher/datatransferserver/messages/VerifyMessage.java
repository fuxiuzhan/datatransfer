package com.fxz.channelswitcher.datatransferserver.messages;


import com.fxz.channelswitcher.datatransferserver.auth.auth.Utils;
import com.fxz.channelswitcher.datatransferserver.constant.Const;
import com.fxz.channelswitcher.datatransferserver.utils.ClientInfo;

public class VerifyMessage extends BaseMessage {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	private ClientInfo clientInfo;

	public VerifyMessage() {
		super(Const.MSG_VERIFY);
	}

	public ClientInfo getClientInfo() {
		return clientInfo;
	}

	public VerifyMessage(BaseMessage baseMessage) {
		super(Const.MSG_VERIFY);
		setAppID(baseMessage.getAppID());
		setSocketUUID(baseMessage.getSocketUUID());
		setUserID(baseMessage.getUserID());
		if (baseMessage.getBody().length > 10) {
			clientInfo = (ClientInfo) Utils.json2Object(new String(baseMessage.getBody()), ClientInfo.class, null);
		}
	}

	public VerifyMessage(String userid, String appid, String socketuuid) {
		super(Const.MSG_VERIFY);
		setAppID(appid);
		setUserID(userid);
		setSocketUUID(socketuuid);

	}

	public VerifyMessage( ClientInfo clientnfo) {
		super(Const.MSG_VERIFY);
		this.clientInfo = clientnfo;
		setAppID(clientnfo.getAppID());
		setUserID(clientInfo.getUserID());
		setSocketUUID("N/A");
		String json = Utils.object2Json(clientnfo);
		setBody(json.getBytes());
	}
}
