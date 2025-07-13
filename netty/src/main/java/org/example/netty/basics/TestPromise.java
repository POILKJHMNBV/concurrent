package org.example.netty.basics;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author zhenwu
 * @date 2025/7/13 12:11
 */
public class TestPromise {
    private static final Logger log = LoggerFactory.getLogger(TestPromise.class);
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(2);
        DefaultPromise<String> promise = new DefaultPromise<>(eventLoopGroup.next());
        eventLoopGroup.execute(() -> {
            log.info("执行计算...");
            promise.setSuccess("hello world");
        });
        promise.addListener(future -> {
            log.info("获取结果：{}", future.getNow());
            eventLoopGroup.shutdownGracefully();
        });
        log.info("获取结果：{}", promise.get());
    }
}
