/*
 * Copyright (c) Travelsky Corp.
 * All Rights Reserved.
 */
package com.wjy.im.client;

import com.wjy.im.client.handler.ClientMessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

/**
 * @author wangjiayou 2019/7/2
 * @version ORAS v1.0
 */
public class IMessageClient {
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientMessageHandler());
                    }
                });
        bootstrap.connect("127.0.0.1", 8000).addListener(future -> {
            if(future.isSuccess()) {
                System.out.println(new Date() + "：连接[127.0.0.1:8000]成功！");
            } else {
                System.out.println(new Date() + "：连接[127.0.0.1:8000]失败！");
            }
        });
    }
}
