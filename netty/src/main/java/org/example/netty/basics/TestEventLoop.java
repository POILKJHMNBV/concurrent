package org.example.netty.basics;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhenwu
 * @date 2025/7/12 19:24
 */
public class TestEventLoop {

    private static final Logger log = LoggerFactory.getLogger(TestEventLoop.class);
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup(2);
//        EventLoopGroup group = new DefaultEventLoop();
        log.info("{}", group.next());
        log.info("{}", group.next());
        log.info("{}", group.next());
        group.execute(() -> log.info("task..."));
        group.shutdownGracefully();
    }
}
