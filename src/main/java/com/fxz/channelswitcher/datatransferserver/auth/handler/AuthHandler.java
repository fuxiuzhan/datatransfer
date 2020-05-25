/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.auth.handler 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月31日 下午5:33:40 
 * 
 */
package com.fxz.channelswitcher.datatransferserver.auth.handler;

import com.fxz.channelswitcher.datatransferserver.auth.auth.IDigest;
import com.fxz.channelswitcher.datatransferserver.auth.auth.INonSymEncrypt;
import com.fxz.channelswitcher.datatransferserver.auth.auth.ISymEncrypt;
import com.fxz.channelswitcher.datatransferserver.auth.auth.Utils;
import com.fxz.channelswitcher.datatransferserver.auth.config.AuthConfig;
import com.fxz.channelswitcher.datatransferserver.auth.encryptions.EncryptFactory;
import com.fxz.channelswitcher.datatransferserver.constant.Const;
import com.fxz.channelswitcher.datatransferserver.messages.AuthMessage;
import com.fxz.channelswitcher.datatransferserver.messages.BaseMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.apache.log4j.Logger;

/**
 * @ClassName: AuthHandler
 * @Description: TODO
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月31日 下午5:33:40
 */

public class AuthHandler extends ChannelHandlerAdapter {

	Logger logger = Logger.getLogger(this.getClass().getName());
	AuthConfig config;
	AttributeKey<String> pub_k = AttributeKey.valueOf("public_key");
	AttributeKey<String> pri_k = AttributeKey.valueOf("private_key");
	AttributeKey<String> r_pub_k = AttributeKey.valueOf("r_public_key");
	AttributeKey<String> auth = AttributeKey.valueOf("auth");
	AttributeKey<String> sess = AttributeKey.valueOf("session");
	Attribute<String> session;
	Attribute<String> isauth;
	Attribute<String> public_key;
	Attribute<String> r_public_key;
	Attribute<String> private_key;
	AttributeKey<String> sess1 = AttributeKey.valueOf("session1");
	Attribute<String> session_key1;
	AttributeKey<String> sess2 = AttributeKey.valueOf("session2");
	Attribute<String> session_key2;

	public AuthHandler(AuthConfig authConfig) {
		this.config = authConfig;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (config.getPrivate_key() != null && config.getPublic_key() != null) {
			public_key = ctx.attr(pub_k);
			private_key = ctx.attr(pri_k);
			public_key.set(config.getPrivate_key());
			private_key.set(config.getPublic_key());
		} else {
			INonSymEncrypt nonSymEncrypt = EncryptFactory.getNonSymEncrypt(config.getNonSymEncryt());
			nonSymEncrypt.init(config.getNonsymBlockSize());
			public_key = ctx.attr(pub_k);
			private_key = ctx.attr(pri_k);
			public_key.set(nonSymEncrypt.getPrivateKey());
			private_key.set(nonSymEncrypt.getPublicKey());
		}
		session = ctx.attr(sess);
		isauth = ctx.attr(auth);
		r_public_key = ctx.attr(r_pub_k);
		r_public_key.set(config.getRemote_public_key());
		session_key1 = ctx.attr(sess1);
		session_key2 = ctx.attr(sess2);
		session = ctx.attr(sess);
		logger.info("private_key->" + private_key.get());
		logger.info("public_key->" + public_key.get());
		if (config.isServer()) {
			// super.channelActive(ctx);
		} else {
			ctx.writeAndFlush(new AuthMessage(Const.AUTH_PUBLIC_KEY, Utils.Hex2Byte(public_key.get())));
			logger.info("Client Send Public_Key");
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		// TODO Auto-generated method stub
		ISymEncrypt symEncrypt = EncryptFactory.getSymEncrypt(config.getSymEncrypt());
		INonSymEncrypt nonSymEncrypt = EncryptFactory.getNonSymEncrypt(config.getNonSymEncryt());
		IDigest digest = EncryptFactory.getDigest(config.getAuthDigest());
		if (session.get() == null || isauth == null) {
			if (((BaseMessage) msg).getMessageType() == Const.MESG_TYPE_AUTH) {
				AuthMessage authMessage = new AuthMessage((BaseMessage) msg);
				switch (authMessage.getSubType()) {
				case Const.AUTH_PUBLIC_KEY:
					// process public_key,send my public_key
					r_public_key.set(Utils.Byte2Hex(authMessage.getData()));
					if (!config.isServer()) {
						session_key1.set(Utils.getRamdomMesg(32));
						byte[] send = nonSymEncrypt.encryptByPublicKey(nonSymEncrypt.encryptByPrivateKey(session_key1.get().getBytes(), private_key.get().toString()), r_public_key.get().toString());
						logger.info("Client SessionKey1->" + session_key1.get());
						ctx.writeAndFlush(new AuthMessage(Const.AUTH_DH_MESSAGE, send));
					} else {
						ctx.writeAndFlush(new AuthMessage(Const.AUTH_PUBLIC_KEY, Utils.Hex2Byte(public_key.get())));
						logger.info("Server Send Public_Key");
					}
					break;
				case Const.AUTH_DH_MESSAGE:
					// get session_key
					// process dh_message,send my dh_message
					if (config.isServer()) {
						session_key2.set(new String(nonSymEncrypt.decryptByPublicKey(nonSymEncrypt.decryptByPrivateKey(authMessage.getData(), private_key.get()), r_public_key.get())));
						session_key1.set(Utils.getRamdomMesg(32));
						byte[] send = nonSymEncrypt.encryptByPublicKey(nonSymEncrypt.encryptByPrivateKey(session_key1.get().getBytes(), private_key.get()), r_public_key.get());
						ctx.writeAndFlush(new AuthMessage(Const.AUTH_DH_MESSAGE, send));
						logger.info("key1->" + session_key1.get() + "   key2->" + session_key2.get());
						session.set(digest.digest(session_key2.get() + session_key1.get()));
						logger.info("sessionkey->" + session.get());
						isauth.set("yes");
					} else {
						session_key2.set(new String(nonSymEncrypt.decryptByPublicKey(nonSymEncrypt.decryptByPrivateKey(authMessage.getData(), private_key.get()), r_public_key.get())));
						session.set(digest.digest(session_key1.get() + session_key2.get()));
						logger.info("key1->" + session_key1.get() + "   key2->" + session_key2.get());
						logger.info("sessionkey->" + session.get());
						isauth.set("yes");
						super.channelActive(ctx);
					}
					break;
				case Const.AUTH_NEED_HD:
					// need dh send public_key message
					break;
				case Const.AUTH_PRIVATE_KEY:
					//
					break;
				default:
					// other unauth operation,exit now
					ctx.close();
					break;
				}
			}
		} else {
			BaseMessage baseMessage = (BaseMessage) msg;
			baseMessage.setBody(symEncrypt.decrypt(baseMessage.getBody(), session.get()));
			super.channelRead(ctx, msg);
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof BaseMessage) {
			if (isauth.get() != null && session.get() != null) {
				BaseMessage baseMessage = (BaseMessage) msg;
				ISymEncrypt symEncrypt = EncryptFactory.getSymEncrypt(config.getSymEncrypt());
				baseMessage.setBody(symEncrypt.encrypt(baseMessage.getBody(), session.get()));
				super.write(ctx, baseMessage, promise);
			} else {
				super.write(ctx, msg, promise);
			}
		} else {
			logger.error("message format error ->" + msg);
		}

	}
}
