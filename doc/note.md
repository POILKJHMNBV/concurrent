# 一、NIO基础

## 1.1 三大组件

### Channel

Channel类似于stream，但是比stream更为底层，Channel可以理解为是IO操作的通道，比如FileChannel、SocketChannel、DatagramChannel、ServerSocketChannel。

### Buffer

Buffer用来缓冲读写数据，Buffer的实现类有ByteBuffer、CharBuffer、ShortBuffer、IntBuffer、LongBuffer、FloatBuffer、DoubleBuffer。

### Selector

Selector是多路复用器，可以理解为是一个线程，通过Selector可以监听多个Channel，当某个Channel发生事件时，Selector会返回该Channel，然后通过该Channel获取数据。