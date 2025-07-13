# 一、NIO基础

## 1.1 三大组件

### Channel

Channel类似于stream，但是比stream更为底层，Channel可以理解为是IO操作的通道，比如FileChannel、SocketChannel、DatagramChannel、ServerSocketChannel。

### Buffer

Buffer用来缓冲读写数据，Buffer的实现类有ByteBuffer、CharBuffer、ShortBuffer、IntBuffer、LongBuffer、FloatBuffer、DoubleBuffer。

### Selector

Selector是多路复用器，可以理解为是一个线程，通过Selector可以监听多个Channel，当某个Channel发生事件时，Selector会返回该Channel，然后通过该Channel获取数据。

# 二、IO模型

阻塞/非阻塞：指用户线程发起系统调用时，系统调用是否会阻塞用户线程。
同步：线程自己获取结果(一个线程)。
异步：线程自己不会获取结果，而是由其他线程送结果(至少两个线程)。

## 2.1 BIO

同步阻塞IO：用户线程在系统调用时阻塞，等待系统调用返回结果，阻塞期间无法响应其他事件。

## 2.2 NIO

同步非阻塞IO：用户线程不阻塞，系统调用后立即返回结果，容易导致CPU空转。

## 2.3 IO多路复用

一种同步IO模型，通过单线程配合Select监听多个IO事件，使得单线程可以同时处理多个IO事件，提高系统的并发能力。

## 2.4 AIO

异步IO：异步IO则是采用“订阅-通知”模式: 即应用程序向操作系统注册IO监听，然后继续做自己的事情。当操作系统发生IO事件，并且准备好数据后，在主动通知应用程序，触发相应的函数。

# 三、Netty

## 3.1 五大组件

### EventLoop

EventLoop是Netty的线程模型核心，EventLoopGroup是EventLoop的集合。EventLoop本质上是一个单线程执行器(同时维护了一个selector)，它负责处理Channel事件，如接收数据、发送数据、处理I/O错误等。EventLoop和channel之间是一对多的关系。

### Channel

### Future & Promise

### ChannelHandler & ChannelPipeline

### ByteBuf