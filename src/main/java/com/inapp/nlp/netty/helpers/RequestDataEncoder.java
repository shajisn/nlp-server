package com.inapp.nlp.netty.helpers;

import com.inapp.nlp.netty.model.RequestData;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RequestDataEncoder extends MessageToByteEncoder<RequestData> {

//    private final Charset charset = Charset.forName("UTF-8");

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestData msg, ByteBuf out) throws Exception {
    	System.out.println("RequestDataEncoder ... Query = " + msg.getRequestQuery());
    	int length = msg.getLength();
    	byte []data = msg.getDataBytes();
    	ByteBuf buff = ctx.alloc().buffer(length);
        buff.writeBytes(data);
        out.writeInt(length);
        out.writeBytes(buff);
    }
}
