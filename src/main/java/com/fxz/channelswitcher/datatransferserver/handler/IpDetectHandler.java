package com.fxz.channelswitcher.datatransferserver.handler;

import com.fxz.channelswitcher.datatransferserver.statistic.ClientConfig;
import com.fxz.channelswitcher.datatransferserver.utils.FileUtils;
import com.fxz.channelswitcher.datatransferserver.utils.TpsLimiter;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * @author xiuzhan.fu
 */
public class IpDetectHandler extends ChannelHandlerAdapter {
    org.apache.log4j.Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel()
                .remoteAddress();
        String peerIp = insocket.getAddress().getHostAddress();
        if (isAllow(peerIp)) {
            super.channelActive(ctx);
        } else {
            ctx.close();
        }
    }

    private boolean isAllow(String peerIp) {
        if (ClientConfig.getWhiteSet().contains(peerIp)) {
            logger.info("whiteIp->" + peerIp + ",skiped..");
            return true;
        }
        if (ClientConfig.getBlackMap().containsKey(peerIp)) {
            logger.warn("ip->" + peerIp + " is in black List ,connect request closed!!!!");
            return false;
        }
        ClientConfig.addLimiter(peerIp, new TpsLimiter(3600 * 1000, ClientConfig.getMaxTryTimes()));
        TpsLimiter limiter = ClientConfig.getLimiter(peerIp);
        if (limiter != null) {
            if (!limiter.isAllow()) {
                ClientConfig.addBlackMap(peerIp);
                logger.warn("ip->" + peerIp + " is be add in black list ,connect request closed!!!!");
                FileUtils.appendFile(ClientConfig.AUTO_DETECT_LIST, peerIp);
                return false;
            }
        }
        return true;
    }


}
