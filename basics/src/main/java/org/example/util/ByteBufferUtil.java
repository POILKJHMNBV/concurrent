package org.example.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * ByteBuffer工具类
 * @author zhenwu
 * @date 2025/7/5 14:18
 */
public final class ByteBufferUtil {
    private ByteBufferUtil() {}

    public static List<String> split(ByteBuffer buffer, char delimiter) {
        if (needFlip(buffer)) {
            buffer.flip();
        }
        List<String> result = new ArrayList<>();
        for (int i = 0, s = -1, n = buffer.limit(); i < n; i++) {
            if (buffer.get(i) == delimiter) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(i + 1 - s);
                for (int j = s + 1; j < i; j++) {
                    byteBuffer.put(buffer.get(j));
                }
                s = i;
                byteBuffer.flip();
                result.add(StandardCharsets.UTF_8.decode(byteBuffer).toString());
            }
        }
        return result;
    }

    public static int readLength(ByteBuffer buffer) {
        // 固定 4 字节表示长度
        byte a = buffer.get(3), b = buffer.get(2), c = buffer.get(1), d = buffer.get(0);
        return (a & 0xff) | ((b & 0xff) << 8) | ((c & 0xff) << 16) | ((d & 0xff) << 24);
    }

    private static boolean needFlip(ByteBuffer buffer) {
        return 0 != buffer.position();
    }

    public static String toString(ByteBuffer buffer) {
        return  "ByteBuffer{" +
                "position=" + buffer.position() +
                ", limit=" + buffer.limit() +
                ", capacity=" + buffer.capacity() +
                ", isReadOnly=" + buffer.isReadOnly() +
                ", isDirect=" + buffer.isDirect() +
                ", isArray=" + buffer.hasArray() +
                ", arrayOffset=" + buffer.arrayOffset();
    }

    public static String read(ByteBuffer buffer) {
        if (needFlip(buffer)) {
            buffer.flip();
        }
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }

    public static ByteBuffer getMsg(String msg) {
        int length = msg.length();
        ByteBuffer buffer = ByteBuffer.allocate(length + 4);
        buffer.putInt(length);
        buffer.put(msg.getBytes());
        buffer.flip();
        return buffer;
    }
}
