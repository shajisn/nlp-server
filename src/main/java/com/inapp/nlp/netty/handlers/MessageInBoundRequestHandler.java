package com.inapp.nlp.netty.handlers;

import java.util.Collection;

import com.inapp.nlp.netty.model.RequestData;
import com.inapp.nlp.netty.model.ResponseData;
import com.inapp.nlp.process.NLPSequenceSearcher;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageInBoundRequestHandler extends SimpleChannelInboundHandler<RequestData> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RequestData msg) throws Exception {
		RequestData requestData = (RequestData) msg;
		System.out.println("Channel Read complete...." + requestData);
		String sQuery = requestData.getRequestQuery();
		String sReturn = "";
		System.out.println("Request received... Query = " + sQuery);
		if (!sQuery.trim().equalsIgnoreCase("Hi")) {
			Collection<String> sWords = null;
			try {
				sWords = NLPSequenceSearcher.getInstance().findNearWords(sQuery);
			} catch (Exception e) {
				e.printStackTrace();
			}
			sReturn = sWords.toString();
			System.out.println("Words = " + sReturn);
		} else
			sReturn = "Hello";
		ResponseData responseData = new ResponseData();
		responseData.setWords(sReturn);
		ctx.write(responseData);
		System.out.println("Sending response ... Data=" + responseData);
		ctx.flush();
	}
}