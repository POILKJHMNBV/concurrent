package org.example.practice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author zhenwu
 * @date 2025/7/7 22:04
 */
public class SimpleClient {
    private static final Logger log = LoggerFactory.getLogger(SimpleClient.class.getName());
    public static void main(String[] args) throws Exception {
        SocketChannel sc = SocketChannel.open();
        log.info("client start...");
        sc.connect(new InetSocketAddress("127.0.0.1", 8080));
        log.info("client connected...");
        sc.write(ByteBuffer.wrap("Hello World!".getBytes()));
//        TimeUnit.SECONDS.sleep(5);
        sc.close();
    }
}
