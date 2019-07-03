/*
 * Copyright (c) Travelsky Corp.
 * All Rights Reserved.
 */
package com.wjy.im2.server.handler;

import com.wjy.protocol.packet.impl.MessageRequestPacket;
import com.wjy.protocol.packet.impl.MessageResponsePacket;
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

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMsg(msg.getMsg());

        ctx.channel().writeAndFlush(messageResponsePacket);
    }
}
