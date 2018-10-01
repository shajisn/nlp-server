package com.inapp.nlp.netty.helpers;

import java.util.List;

import com.inapp.nlp.netty.model.RequestData;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * The biggest difference between ReplayingDecoder and ByteToMessageDecoder is
 * that ReplayingDecoder allows you to implement the decode() and decodeLast()
 * methods just like all required bytes were received already, rather than
 * checking the availability of the required bytes.
 * 
 * @author InApp
 *
 */
public class RequestDecoder extends ReplayingDecoder<RequestData> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {

		if (!buf.isReadable())
			return;

		byte[] bytes;
		int length = buf.readInt();
		System.out.println("Request buffer length " + length);
		if (length > 1000) {
			System.out.println("Cannot process requests with characters length = " + length);
			return;
		}

		if (buf.hasArray()) {
			bytes = buf.array();
		} else {
			bytes = new byte[length];
			buf.getBytes(buf.readerIndex(), bytes);
		}

		String query = new String(bytes);
		System.out.println("Request decoder ... Query = " + query);
		RequestData data = new RequestData(query);
		out.add(data);
	}

}