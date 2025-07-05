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
}
