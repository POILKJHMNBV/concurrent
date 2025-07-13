package org.example.netty.basics;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.example.netty.util.ByteBufUtil;

import java.nio.charset.Charset;

/**
 * ByteBuf 相较于传统 ByteBuffer 的优势
 * 1. 池化思想 - 减少内存分配和释放的次数，从而提升性能
 * 2. 读写指针分离，不像 ByteBuffer 共用读写指针
 * 3. 支持自动扩容
 * 4. 支持链式调用
 * 5. 很多地方体现零拷贝的思想，例如slice、duplicate、compositeBuffer
 * @author zhenwu
 * @date 2025/7/13 15:33
 */
public class TestByteBuf {
    public static void main(String[] args) {
        testSlice();
    }

    /**
     * ByteBuf 通过引用计数进行内存管理
     */
    private static void testRelease() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeCharSequence("hello world", Charset.defaultCharset());

        // 增加引用计数
        byteBuf.retain();

        // 减少引用计数
        byteBuf.release();
    }

    /**
     * addComponents方法可以将多个 ByteBuf 逻辑组合成一个新的 ByteBuf，仍然和原始的 ByteBuf 共享同一块内存
     */
    private static void testComposite() {
        ByteBuf f1 = ByteBufAllocator.DEFAULT.buffer(16);
        f1.writeCharSequence("hello ", Charset.defaultCharset());

        // 新建固定容量的 ByteBuf
        ByteBuf f2 = Unpooled.wrappedBuffer("world".getBytes(Charset.defaultCharset()));
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        compositeByteBuf.addComponents(true, f1, f2);
        System.out.println(ByteBufUtil.formatByteBuf(compositeByteBuf));
    }

    /**
     * slice方法对 ByteBuf 进行逻辑切片，不会改变 ByteBuf 的容量，切片后的 ByteBuf 和原有的 ByteBuf 共享内存，不可进行自动扩容
     * f1 和 f2 指向同一个内存区域，维护不同的读写指针
     */
    private static void testSlice() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeCharSequence("hello world", Charset.defaultCharset());
        System.out.println(ByteBufUtil.formatByteBuf(byteBuf));
        ByteBuf f1 = byteBuf.slice(0, 5);
        System.out.println(ByteBufUtil.formatByteBuf(f1));
        ByteBuf f2 = byteBuf.slice(6, 5);
        System.out.println(ByteBufUtil.formatByteBuf(f2));
    }

    /**
     * duplicate方法对 ByteBuf 进行逻辑复制，复制后的 ByteBuf 是一个物理内存的引用，对复制后的 ByteBuf 的修改会影响到原 ByteBuf
     * 两个 ByteBuf 只是维护了不同的读写指针
     */
    private static void testDuplicate() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(12);
        byteBuf.writeCharSequence("hello world", Charset.defaultCharset());
        System.out.println(ByteBufUtil.formatByteBuf(byteBuf));
        ByteBuf newByteBuf = byteBuf.duplicate();
        System.out.println(ByteBufUtil.formatByteBuf(newByteBuf));
        newByteBuf.writeCharSequence("hello world", Charset.defaultCharset());
        byteBuf.writeCharSequence("hello Java", Charset.defaultCharset());
        System.out.println(ByteBufUtil.formatByteBuf(byteBuf));
        System.out.println(ByteBufUtil.formatByteBuf(newByteBuf));
    }

    /**
     * copy方法复制出来的是完全不同的 ByteBuf，和原来的 ByteBuf 内存地址不同
     */
    private static void testCopy() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(12);
        byteBuf.writeCharSequence("hello world", Charset.defaultCharset());
        System.out.println(ByteBufUtil.formatByteBuf(byteBuf));
        ByteBuf newByteBuf = byteBuf.copy();
        System.out.println(ByteBufUtil.formatByteBuf(newByteBuf));
        newByteBuf.writeCharSequence("hello world", Charset.defaultCharset());
        byteBuf.writeCharSequence("hello Java", Charset.defaultCharset());
        System.out.println(ByteBufUtil.formatByteBuf(byteBuf));
        System.out.println(ByteBufUtil.formatByteBuf(newByteBuf));
    }
}
