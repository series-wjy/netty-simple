/*
 * Copyright (c) Travelsky Corp.
 * All Rights Reserved.
 */
package com.wjy.im2.server.handler;

import com.wjy.protocol.packet.impl.LoginRequestPacket;
import com.wjy.protocol.packet.impl.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * @author wangjiayou 2019/7/3
 * @version ORAS v1.0
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {
        System.out.println(new Date() + "：服务端接受登录消息......");

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(msg.getVersion());
        if(valid(msg)) {
            loginResponsePacket.setSuccess(true);
        } else {
            loginResponsePacket.setSuccess(false);
            loginResponsePacket.setReason("账号或密码效验失败！");
        }
        ctx.channel().writeAndFlush(loginResponsePacket);
        ctx.channel().write(loginResponsePacket);
    }

    private boolean valid(LoginRequestPacket packet) {
        return true;
    }
}
