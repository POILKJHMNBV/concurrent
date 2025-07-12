package org.example.practice;

import org.example.util.ByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


/**
 * @author zhenwu
 * @date 2025/7/6 16:03
 */
public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);
    public static void main(String[] args) throws Exception {
        try (Selector selector = Selector.open();
             SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.keys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        ByteBuffer lenBuffer = ByteBuffer.allocate(4);
                        int len = socketChannel.read(lenBuffer);
                        if (len <= 0) {
                            log.error("client {} disconnected...", socketChannel);
                            socketChannel.close();
                        } else {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(ByteBufferUtil.readLength(lenBuffer));
                            len = socketChannel.read(byteBuffer);
                            if (len <= 0) {
                                log.error("client {} disconnected...", socketChannel);
                                socketChannel.close();
                            } else {
                                log.info("read info: {}", ByteBufferUtil.read(byteBuffer));
                                socketChannel.write(ByteBufferUtil.getMsg("Hello Server!"));
                            }
                        }
                    } else if (key.isConnectable()) {
                        if (socketChannel.finishConnect()) { // 完成连接
                            log.info("连接建立成功");
                            key.interestOps(key.interestOps() ^ SelectionKey.OP_CONNECT);
                            ByteBuffer byteBuffer = ByteBufferUtil.getMsg("Hello Server!");
                            socketChannel.write(byteBuffer); //  此处写入安全
                        }
                    }
                }
            }
        }
    }
}
