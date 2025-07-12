package org.example.practice;

import org.example.util.ByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author zhenwu
 * @date 2025/7/6 15:00
 */
public class Worker extends Thread {
    private static final Logger log = LoggerFactory.getLogger(Worker.class);
    private final Selector selector;
    private boolean isShutdown = true;
    public Worker(String name) throws IOException {
        super(name);
        selector = Selector.open();
    }
    @Override
    public void run() {
        isShutdown = false;
        try {
            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (!key.isValid()) {
                        continue;
                    }

                    SocketChannel sc = (SocketChannel) key.channel();
                    if (key.isReadable()) {
                        log.info("socketChannel: {}, trigger event: {}", sc, "read");
                        ByteBuffer lenBuffer = ByteBuffer.allocate(4);
                        int len = sc.read(lenBuffer);
                        if (len <= 0) {
                            log.error("client {} disconnected...", sc);
                            sc.close();
                        } else {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(ByteBufferUtil.readLength(lenBuffer));
                            len = sc.read(byteBuffer);
                            if (len <= 0) {
                                log.error("client {} disconnected...", sc);
                                sc.close();
                            } else {
                                log.info("read info: {}", ByteBufferUtil.read(byteBuffer));
                                sc.write(ByteBufferUtil.getMsg("Hello Client!"));
                            }
                        }
                    } else if (key.isWritable()) {
                        log.info("socketChannel: {}, trigger event: {}", sc, "write");
                        key.cancel();
                    }
                }
            }
        } catch (Exception e) {
            log.error("Worker run error: ", e);
        }
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    public void register(SocketChannel channel) throws IOException {
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ, null);
        selector.wakeup();
    }
}
