package org.example.netty.basics;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author zhenwu
 * @date 2025/7/13 12:05
 */
public class TestFuture {
    private static final Logger log = LoggerFactory.getLogger(TestFuture.class);
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoopGroup eventLoopGroup = new DefaultEventLoop();
        Future<Integer> future = eventLoopGroup.submit(() -> {
            log.info("执行计算...");
            return 10;
        });
        future.addListener(f -> log.info("获取计算结果：{}", f.getNow()));
        log.info("获取计算结果：{}", future.get());
        eventLoopGroup.shutdownGracefully();
    }
}
