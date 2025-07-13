package org.example.netty.basics;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author zhenwu
 * @date 2025/7/12 18:37
 */
public class HelloClient {
    private static final Logger log = LoggerFactory.getLogger(HelloClient.class);
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(2);
        ChannelFuture channelFuture = new Bootstrap()
                .group(eventExecutors)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 连接建立后调用此方法
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("msg: {}", msg);
                                Channel channel = ctx.channel();
//                                channel.writeAndFlush(ctx.alloc().buffer().writeBytes("hello server!".getBytes()));
                                super.channelRead(ctx, msg);
                            }
                        });
                        pipeline.addLast(new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                // connect方法是异步非阻塞的，所以这里会返回一个ChannelFuture对象，并且调用sync方法会阻塞当前线程，直到ChannelFuture对象完成
                .connect(new InetSocketAddress("127.0.0.1", 8080));

//        channelFuture
//                .sync()
//                .channel()
//                .writeAndFlush("hello world");

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                Channel channel = channelFuture.channel();
                channel.closeFuture().addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        log.info("channel is closed");
                        eventExecutors.shutdownGracefully();
                    }
                });
                channel.writeAndFlush("hello world");
//                channel.close();
            }
        });
    }
}
