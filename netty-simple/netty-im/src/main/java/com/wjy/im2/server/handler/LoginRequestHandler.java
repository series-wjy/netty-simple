/*
 * Copyright (c) Travelsky Corp.
 * All Rights Reserved.
 */
package com.wjy.im2.server.handler;

import com.wjy.im2.session.Session;
import com.wjy.im2.session.SessionUtil;
import com.wjy.protocol.packet.impl.LoginRequestPacket;
import com.wjy.protocol.packet.impl.LoginResponsePacket;
import com.wjy.util.IDUtil;
import com.wjy.util.LogUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wangjiayou 2019/7/3
 * @version ORAS v1.0
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {
        LogUtil.print("服务端接受登录消息");

        String userId = IDUtil.randomId();

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setUserId(userId);
        loginResponsePacket.setVersion(msg.getVersion());
        if(valid(msg)) {
            loginResponsePacket.setSuccess(true);
            // 登录成功标识
            SessionUtil.bindSession(new Session(userId, msg.getUsername()), ctx.channel());
        } else {
            loginResponsePacket.setSuccess(false);
            loginResponsePacket.setReason("账号或密码效验失败！");
        }
        ctx.channel().writeAndFlush(loginResponsePacket);
        //ctx.channel().write(loginResponsePacket); // 批量发送，速度更快
    }

    private boolean valid(LoginRequestPacket packet) {
        return true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
    }
}
