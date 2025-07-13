package org.example.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;

/**
 * @author zhenwu
 * @date 2025/7/13 16:19
 */
public final class ByteBufUtil {
    private ByteBufUtil() {}

    public static String formatByteBuf(ByteBuf byteBuf) {
        int length = byteBuf.readableBytes();
        if (length == 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        buf.append(byteBuf).append(StringUtil.NEWLINE);
        io.netty.buffer.ByteBufUtil.appendPrettyHexDump(buf, byteBuf);
        return buf.toString();
    }
}
