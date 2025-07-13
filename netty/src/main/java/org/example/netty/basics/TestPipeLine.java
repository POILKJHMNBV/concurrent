package org.example.netty.basics;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhenwu
 * @date 2025/7/13 12:41
 */
public class TestPipeLine {
    private static final Logger log = LoggerFactory.getLogger(TestPipeLine.class);
    public static void main(String[] args) {
        ChannelFuture channelFuture = new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
                        pipeline.addLast("logHandler", new LoggingHandler(LogLevel.INFO))
                                .addLast(new StringDecoder())
                                .addLast("h1", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {
                                        log.info("h1 begin handle msg: {}", o);
                                        super.channelRead(ctx, o);
                                    }
                                }).addLast("h2", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        log.info("h2 begin handle msg: {}", msg);
                                        super.channelRead(ctx, msg);
                                    }
                                }).addLast("h3", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        log.info("h3 begin handle msg: {}", msg);
                                        Channel channel = ctx.channel();
                                        channel.writeAndFlush(ctx.alloc().buffer().writeBytes("hello client!".getBytes()));
                                    }
                                }).addLast("h4", new ChannelOutboundHandlerAdapter() {
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        log.info("h4 begin handle msg: {}", msg);
                                        super.write(ctx, msg, promise);
                                    }
                                }).addLast("h5", new ChannelOutboundHandlerAdapter() {
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        log.info("h5 begin handle msg: {}", msg);
                                        super.write(ctx, msg, promise);
                                    }
                                }).addLast("h6", new ChannelOutboundHandlerAdapter() {
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        log.info("h6 begin handle msg: {}", msg);
                                        super.write(ctx, msg, promise);
                                    }
                                });
                    }
                })
                .bind(8080);
        channelFuture.addListener((ChannelFutureListener) future -> log.info("server start..."));
    }
}
