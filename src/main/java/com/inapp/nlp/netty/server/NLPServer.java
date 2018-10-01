package com.inapp.nlp.netty.server;

import com.inapp.nlp.netty.handlers.MessageInBoundRequestHandler;
import com.inapp.nlp.netty.helpers.RequestDecoder;
import com.inapp.nlp.netty.helpers.ResponseDataEncoder;
import com.inapp.nlp.process.NLPSequenceSearcher;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NLPServer {
	private int port;

	private NLPServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 9191;
		}
		//Load the NLP singleton on startup...
		System.out.println("Loading NLP Search engine ...");
		NLPSequenceSearcher.getInstance();
		System.out.println("Starting the server ...");
		new NLPServer(port).run();
	}

	private void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.DEBUG))
				.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							/**
							 * Define inbound and outbound handlers that will process requests and output in
							 * the correct order.
							 **/
							ch.pipeline().addLast(new RequestDecoder(), 
									new ResponseDataEncoder(),
									new MessageInBoundRequestHandler());
						}
					})
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
