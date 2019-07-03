/*
 * Copyright (c) Travelsky Corp.
 * All Rights Reserved.
 */
package com.wjy.protocol.packet.codec;

import com.wjy.protocol.packet.Packet;
import com.wjy.protocol.packet.impl.LoginRequestPacket;
import com.wjy.protocol.packet.impl.LoginResponsePacket;
import com.wjy.protocol.packet.impl.MessageRequestPacket;
import com.wjy.protocol.packet.impl.MessageResponsePacket;
import com.wjy.protocol.serialize.Serializer;
import com.wjy.protocol.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

import static com.wjy.protocol.command.Command.LOGIN_REQUEST;
import static com.wjy.protocol.command.Command.LOGIN_RESPONSE;
import static com.wjy.protocol.command.Command.MESSAGE_REQUEST;
import static com.wjy.protocol.command.Command.MESSAGE_RESPONSE;

/**
 * @author wangjiayou 2019/7/1
 * @version ORAS v1.0
 */
public class PacketCodec {
    private static final int MAGIC_NUMBER = 0x12345678;

    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;

    private PacketCodec() {

    }

    public static PacketCodec getInstance() {
        return PacketCodecSingletonHolder.singleton;
    }


    private static class PacketCodecSingletonHolder {
        private static PacketCodec singleton = new PacketCodec();
    }

    static {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    /**
     * 协议包编码
     * @auther wangjiayou
     * @date 2019/7/1
     * @param packet
     * @return io.netty.buffer.ByteBuf
     */
    public ByteBuf encode(Packet packet) {
        // 1、创建ByteBuf对象
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        packetSerialize(packet, byteBuf);

        return byteBuf;
    }

    private void packetSerialize(Packet packet, ByteBuf byteBuf) {
        // 2、序列化协议对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 3、协议拼装
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    /**
     * 协议包编码
     * @auther wangjiayou
     * @date 2019/7/2
     * @param byteBuf
     * @param packet
     * @return io.netty.buffer.ByteBuf
     */

    public ByteBuf encode(ByteBuf byteBuf, Packet packet) {
        // 1、创建ByteBuf对象
        ByteBuf buffer = byteBuf;

        // 2、序列化协议对象
        packetSerialize(packet, buffer);
        return buffer;
    }

    /**
     * 协议包解码
     * @auther wangjiayou
     * @date 2019/7/1
     * @param byteBuf
     * @return com.wjy.protocol.Packet
     */
    public Packet decode(ByteBuf byteBuf) {
        // 跳过magic number
        byteBuf.skipBytes(4);

        // 跳过版本
        byteBuf.skipBytes(1);

        // 序列化算法
        byte serializerAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 消息内容长度
        int length = byteBuf.readInt();

        // 消息内容
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        // 协议类型
        Class<? extends Packet> requestType = getRequestType(command);
        // 序列化方式
        Serializer serializer = getSerializer(serializerAlgorithm);

        if(requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }
        return null;
    }

    private Serializer getSerializer(byte serializerAlgorithm) {
        return serializerMap.get(serializerAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }
}
