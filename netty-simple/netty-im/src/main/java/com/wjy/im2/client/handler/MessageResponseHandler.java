/*
 * Copyright (c) Travelsky Corp.
 * All Rights Reserved.
 */
package com.wjy.im2.client.handler;

import com.wjy.protocol.packet.impl.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;

import java.util.Date;

/**
 * @author wangjiayou 2019/7/3
 * @version ORAS v1.0
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket msg) throws Exception {
        if(!StringUtil.isNullOrEmpty(msg.getMsg())) {
            String fromUserId = msg.getFromUserId();
            String fromUserName = msg.getFromUserName();
            System.out.println(new Date() + "：" + fromUserId + "：" + fromUserName + "：[" + msg.getMsg() + "]");
        }
    }
}
