package org.example.basics;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 利用buffer读取文件数据
 * @author zhenwu
 * @date 2025/6/29 18:35
 */
public class BufferAndChannelTest {
    public static void main(String[] args) {
        try (FileChannel channel = new FileInputStream("./file/data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (channel.read(buffer) != -1) {
                channel.read(buffer);
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
