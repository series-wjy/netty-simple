/*
 * Copyright (c) Travelsky Corp.
 * All Rights Reserved.
 */
package com.wjy.im2.client;

import com.wjy.im2.client.handler.LoginResponseHandler;
import com.wjy.im2.client.handler.MessageResponseHandler;
import com.wjy.im2.coder.PacketDecoder;
import com.wjy.im2.coder.PacketEncoder;
import com.wjy.im2.session.SessionUtil;
import com.wjy.protocol.packet.impl.LoginRequestPacket;
import com.wjy.protocol.packet.impl.MessageRequestPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Date;
import java.util.Scanner;

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
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7,4));
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginResponseHandler());
                        ch.pipeline().addLast(new MessageResponseHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });
        bootstrap.connect("127.0.0.1", 8000).addListener(future -> {
            if(future.isSuccess()) {
                System.out.println(new Date() + "：连接[127.0.0.1:8000]成功！");
                Channel channel = ((ChannelFuture)future).channel();
                startConsoleThread(channel);
            } else {
                System.out.println(new Date() + "：连接[127.0.0.1:8000]失败！");
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        Scanner sc = new Scanner(System.in);
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if(!SessionUtil.hasLogin(channel)) {
                    System.out.println("请输入用户名登录：");
                    String userName = sc.nextLine();
                    loginRequestPacket.setUsername(userName);
                    loginRequestPacket.setPassword("pwd");

                    channel.writeAndFlush(loginRequestPacket);
                    waitForLoginResponse();
               } else {
                    String toUserId = sc.next();
                    String message = sc.next();
                    channel.writeAndFlush(new MessageRequestPacket(toUserId, message));
                }
            }
        }).start();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }
    }
}
