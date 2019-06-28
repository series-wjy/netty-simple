/*
 * Copyright (c) Travelsky Corp.
 * All Rights Reserved.
 */
package com.wjy.im;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author wangjiayou 2019/6/28
 * @version ORAS v1.0
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ":服务端读到数据 ->" + byteBuf.toString(Charset.forName("utf-8")));

        ByteBuf retMsg = getByteBuf(ctx);

        System.out.println(new Date() + "：服务端写出数据");
        ctx.writeAndFlush(retMsg);
    }

    public ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = ctx.alloc().buffer();
        byte[] bytes = "你好，我是隔壁老王，欢迎加我的微信！".getBytes(Charset.forName("utf-8"));
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }
}
