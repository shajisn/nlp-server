package com.inapp.nlp.netty.helpers;

import java.util.List;

import com.inapp.nlp.netty.model.ResponseData;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class ResponseDataDecoder extends ReplayingDecoder<ResponseData> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		if (!in.isReadable())
			return;

		byte[] bytes;
		int length = in.readInt();
		if (length > 25)
			return;
		System.out.println("Response buffer length " + length);
		if (in.hasArray()) {
			bytes = in.array();
		} else {
			bytes = new byte[length];
			in.getBytes(in.readerIndex(), bytes);
		}

		String response = new String(bytes);
		System.out.println("ResponseDataDecoder ..." + response);
		ResponseData data = new ResponseData();
		data.setWords(response);
		out.add(data);
	}
}