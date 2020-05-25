package com.fxz.channelswitcher.datatransferserver.messages.Handler;

import com.fxz.channelswitcher.datatransferserver.messages.BaseMessage;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;


/** 
 * @ClassName: CtrlHandler 
 * @Description: 实现对服务端的远程管理，只对具有管理员权限的用户开放
 * @author: fuxiuzhan@163.com
 * @date: 2018年10月12日 下午2:01:12  
 */
public class CtrlHandler implements IProcessMessage {

	@Override
	public void process(ChannelHandlerContext ctx, BaseMessage baseMessage) throws IOException {
		// TODO Auto-generated method stub
		
	}

}

