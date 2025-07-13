package org.example.practice;

import org.example.util.ByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;

/**
 * File AIO测试
 * @author zhenwu
 * @date 2025/7/12 17:04
 */
public class FileAioDemo {

    private static final Logger log = LoggerFactory.getLogger(FileAioDemo.class);
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1);
        ByteBuffer byteBuffer = ByteBuffer.allocate(9);
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(Paths.get("./file/data.txt"), StandardOpenOption.READ)) {
            log.info("read start...");
            fileChannel.read(byteBuffer, 0, byteBuffer, new CompletionHandler<>() {
                @Override
                public void completed(Integer len, ByteBuffer attachment) {
                    String msg = ByteBufferUtil.read(attachment);
                    log.info("read complete, len: {}, msg: {}", len, msg);
                    latch.countDown();
                }
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    log.error("read error: ", exc);
                }
            });
            log.info("read end...");
            latch.await();
        } catch (Exception e) {
            log.error("read error", e);
        }
    }
}
