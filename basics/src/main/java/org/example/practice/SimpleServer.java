package org.example.practice;

import org.example.util.ByteBufferUtil;
import org.example.util.ChannelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author zhenwu
 * @date 2025/7/7 21:58
 */
public class SimpleServer {
    private static final Logger log = LoggerFactory.getLogger(SimpleServer.class);
    public static void main(String[] args) {
        try (ServerSocketChannel ssc = ServerSocketChannel.open();
             Selector selector = Selector.open()) {
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(8080));
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                log.info("Waiting for client...");
                int selectKeys = selector.select();
//                log.info("selectKeys: {}", selectKeys);
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        SocketChannel sc = ssc.accept();
                        log.info("Accepted: {}", sc);
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    } else if (key.isReadable()) {
                        log.info("trigger event: {}", "read");
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(12);
                        int len = sc.read(byteBuffer);
                        log.info("read length: {}", len);
                        if (len <= 0) {
                            key.cancel();
                            sc.close();
                        } else {
                            log.info("read info: {}", ByteBufferUtil.read(byteBuffer));
                        }
                    } else if (key.isWritable()) {
                        log.info("trigger event: {}", "write");
                        key.cancel();
                    }
                }
            }
        } catch (IOException e) {
            log.error("Server start error...", e);
        }
    }
}
