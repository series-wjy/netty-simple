/*
 * Copyright (c) Travelsky Corp.
 * All Rights Reserved.
 */
package com.wjy.im2.server.handler;

import com.wjy.im2.session.Session;
import com.wjy.im2.session.SessionUtil;
import com.wjy.protocol.packet.impl.MessageRequestPacket;
import com.wjy.protocol.packet.impl.MessageResponsePacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * @author wangjiayou 2019/7/3
 * @version ORAS v1.0
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) throws Exception {
        System.out.println(new Date() + "：服务端收到消息[" + msg.getMsg() + "]");

        // 1、获取消息发送方的会话信息
        Session session = SessionUtil.getSession(ctx.channel());

        // 2、通过消息发送方的会话信息构造要发送的消息
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUserName(session.getUserName());
        messageResponsePacket.setMsg(msg.getMsg());

        // 3、获取消息接收方的Channel
        Channel toUserChannel = SessionUtil.getChannel(msg.getToUserId());

        // 4、将消息转发给消息接收方
        if(SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            messageResponsePacket.setMsg("对方不在线，请稍后重试！");
            ctx.channel().writeAndFlush(messageResponsePacket);
        }
    }
}
