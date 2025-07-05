package org.example.basics;

import org.example.util.ByteBufferUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * Bytebuffer 演示
 * @author zhenwu
 * @date 2025/7/5 14:16
 */
public class ByteBufferTest {
    public static void main(String[] args) {
        test1();
    }

    private static void test6() {
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode("Hello World!\nHello!\n");
        System.out.println(ByteBufferUtil.split(byteBuffer, '\n'));
    }

    private static void test5() {
        // 将多个ByteBuffer写入文件
        try (FileChannel rw = new RandomAccessFile("./file/language.txt", "rw").getChannel()) {
            ByteBuffer buffer1 = StandardCharsets.UTF_8.encode("Java");
            ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("Python");
            ByteBuffer buffer3 = StandardCharsets.UTF_8.encode("C++");
            long len = rw.write(new ByteBuffer[]{buffer1, buffer2, buffer3});
            System.out.println("写入的字节数：" + len);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void test4() {
        // 将文件数据读取到多个ByteBuffer中
        try (FileChannel channel = new RandomAccessFile("./file/data.txt", "rw").getChannel()) {
            ByteBuffer buffer1 = ByteBuffer.allocate(3);
            ByteBuffer buffer2 = ByteBuffer.allocate(3);
            ByteBuffer buffer3 = ByteBuffer.allocate(3);
            long len = channel.read(new ByteBuffer[]{buffer1, buffer2, buffer3});
            System.out.println("读取的字节数：" + len);
            System.out.println(ByteBufferUtil.read(buffer1));
            System.out.println(ByteBufferUtil.read(buffer2));
            System.out.println(ByteBufferUtil.read(buffer3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void test3() {
        // 字符串转为ByteBuffer
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode("Hello World!");

        ByteBuffer wrapped = ByteBuffer.wrap("Hello World!".getBytes());
    }

    private static void test2() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.put("Hello World!".getBytes());
        byteBuffer.flip();
        for (int i = 0, n = byteBuffer.limit(); i < n; i++) {
            System.out.print((char) byteBuffer.get(i));
        }
        System.out.println("\nread => " + ByteBufferUtil.toString(byteBuffer));
    }

    private static void test1() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        System.out.println("init => " + ByteBufferUtil.toString(byteBuffer));
        byteBuffer.put("Hello World!".getBytes());
        System.out.println("write => " + ByteBufferUtil.toString(byteBuffer));
        byteBuffer.flip();
        System.out.println("read => " + ByteBufferUtil.toString(byteBuffer));
        byteBuffer.get();
        System.out.println("read a char => " + ByteBufferUtil.toString(byteBuffer));
        byteBuffer.compact();
        System.out.println("compact => " + ByteBufferUtil.toString(byteBuffer));
    }
}
