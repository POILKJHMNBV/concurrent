package org.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * @author zhenwu
 * @date 2025/7/12 15:38
 */
public final class ChannelUtil {
    private static final Logger log = LoggerFactory.getLogger(ChannelUtil.class);
    private ChannelUtil() {
    }

    public static void printInfo(SocketChannel socketChannel) {
        try {
            log.info("-----------------------------------------------------------------");
            log.info("socketChannel: {}", socketChannel);
            log.info("socketChannel.getLocalAddress(): {}", socketChannel.getLocalAddress());
            log.info("socketChannel.getRemoteAddress(): {}", socketChannel.getRemoteAddress());
            log.info("socketChannel.isConnected(): {}", socketChannel.isConnected());
            log.info("socketChannel.isOpen(): {}", socketChannel.isOpen());
            log.info("socketChannel.isBlocking(): {}", socketChannel.isBlocking());
            log.info("-----------------------------------------------------------------");
        } catch (Exception e) {
            log.error("printInfo error: ", e);
        }
    }
}
