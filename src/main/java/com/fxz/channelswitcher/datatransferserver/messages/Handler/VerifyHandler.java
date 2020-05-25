package com.fxz.channelswitcher.datatransferserver.messages.Handler;

import com.fxz.channelswitcher.datatransferserver.constant.Const;
import com.fxz.channelswitcher.datatransferserver.constant.Params;
import com.fxz.channelswitcher.datatransferserver.ctrl.UserCtrl;
import com.fxz.channelswitcher.datatransferserver.messages.BaseMessage;
import com.fxz.channelswitcher.datatransferserver.messages.MsgMessage;
import com.fxz.channelswitcher.datatransferserver.messages.ResultMessage;
import com.fxz.channelswitcher.datatransferserver.messages.VerifyMessage;
import com.fxz.channelswitcher.datatransferserver.utils.ClientInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.apache.log4j.Logger;

import java.io.IOException;



/**
 * @ClassName: VerifyHandler
 * @Description: 对客户端的进行合法性验证
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月12日 下午1:59:58
 */
public class VerifyHandler implements IProcessMessage {
    Logger logger = Logger.getLogger(this.getClass().getName());
    AttributeKey<String> socketid = AttributeKey.valueOf("socketid");
    Attribute<String> socketId;
    AttributeKey<String> clientid = AttributeKey.valueOf("clientid");
    Attribute<String> clientId;
    AttributeKey<ClientInfo> clientinfo = AttributeKey.valueOf("clientinfo");
    Attribute<ClientInfo> clientAttr;

    @Override
    public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws IOException {
        // TODO Auto-generated method stub
        VerifyMessage verifymsg = new VerifyMessage(baseMessage);
        String userid = verifymsg.getUserID();
        String appid = verifymsg.getAppID();
        /*
         * body為客戶端主機信息JSON
         */
        if (UserCtrl.verifyUser(userid, appid)) {
            // 驗證成功
            logger.info("VerifyUser ....OK!");

            // 增加到在线列表当中，会在建立ChannelPare时进行是否存活检查
            /*
             * 验证通过，增加在线个数检查，已经在线的客户端，不能重复登录
             */
            if (Params.getOnlineClient(verifymsg.getClientInfo().getClientID()) != null) {

                ClientInfo clientInfo = Params.getClientInfo(verifymsg.getClientInfo().getClientID());
                if (clientInfo.isEnableReplace()) {
                    Channel channel = Params.getOnlineClient(clientInfo.getClientID());
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (Exception e) {
                            logger.error(e);
                        }
                    }
                    Params.addOnLineClient(verifymsg.getClientInfo().getClientID(), ctx.channel());
                    clientId = ctx.attr(clientid);
                    clientId.set(verifymsg.getClientInfo().getClientID());
                    socketId = ctx.attr(socketid);
                    socketId.set(verifymsg.getClientInfo().getClientID());
                    clientAttr = ctx.attr(clientinfo);
                    clientAttr.set(verifymsg.getClientInfo());

                    ctx.writeAndFlush(new ResultMessage(Const.RTN_VERIFY, "access_ok", true));
                    ctx.writeAndFlush(new MsgMessage());
                } else {
                    /*
                     * 已经在线，不能登录
                     */
                    logger.info("UserId->" + verifymsg.getClientInfo().getClientID() + "  already logined!");
                    ctx.writeAndFlush(new ResultMessage(Const.RTN_VERIFY, "client already logined!", false));

                }
            } else {
                Params.addOnLineClient(verifymsg.getClientInfo().getClientID(), ctx.channel());
                clientId = ctx.attr(clientid);
                clientId.set(verifymsg.getClientInfo().getClientID());
                socketId = ctx.attr(socketid);
                socketId.set(verifymsg.getClientInfo().getClientID());
                ctx.writeAndFlush(new ResultMessage(Const.RTN_VERIFY, "access_ok", true));
            }

        } else {
            // 驗證失敗
            ctx.writeAndFlush(new ResultMessage(Const.RTN_VERIFY, "Error", false));
            if (verifymsg.getClientInfo() != null) {
                logger.info("clientInfo->" + verifymsg.getClientInfo());
            }
            logger.info("Verify Failed");
            ctx.close();
        }
    }

}
