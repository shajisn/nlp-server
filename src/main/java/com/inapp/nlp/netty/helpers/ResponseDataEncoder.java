package com.inapp.nlp.netty.helpers;

import com.inapp.nlp.netty.model.ResponseData;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ResponseDataEncoder extends MessageToByteEncoder<ResponseData> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ResponseData msg, ByteBuf out) throws Exception {
		System.out.println("ResponseDataEncoder ... Encoding : " + msg.getWords());
		int length = msg.getLength();
		byte[] data = msg.getDataBytes();
		ByteBuf buff = ctx.alloc().buffer(length);
		buff.writeBytes(data);
		out.writeInt(length);
		out.writeBytes(buff);
	}
}