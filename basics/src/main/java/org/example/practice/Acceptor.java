package org.example.practice;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * @author zhenwu
 * @date 2025/7/6 15:00
 */
public class Acceptor extends Thread {
    private static final Logger log = LoggerFactory.getLogger(Acceptor.class);
    private static final String DEFAULT_NAME = "acceptor";
    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_WORKER_NUM = 2;
    private static final Random random = new Random();
    private final Selector selector;
    private final List<Worker> workerList;
    private int port = DEFAULT_PORT;
    private int workerNum = DEFAULT_WORKER_NUM;
    public Acceptor() throws IOException {
        this(DEFAULT_NAME);
    }
    public Acceptor(String name) throws IOException {
        super(StrUtil.isBlank(name) ? DEFAULT_NAME : name);
        this.selector = Selector.open();
        this.workerList = new ArrayList<>(workerNum);
        for (int i = 0; i < workerNum; i++) {
            workerList.add(new Worker("worker-" + i));
        }
    }

    public Acceptor(String name, int port, int workerNum) throws IOException {
        this(name);
        this.port = port;
        this.workerNum = workerNum;
    }

    private Worker randomPickWorker() {
        return workerList.get(random.nextInt(workerNum));
    }

    private ServerSocketChannel init() throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        log.info("Acceptor start at port: {}", port);
        return ssc;
    }

    @Override
    public void run() {
        ServerSocketChannel ssc = null;
        try {
            ssc = init();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    SocketChannel sc = ssc.accept();
                    log.info("accept: {}", sc);
                    Worker worker = randomPickWorker();
                    worker.register(sc);
                    if (worker.isShutdown()) {
                        worker.start();
                    }
                }
            }
        } catch (IOException e) {
            log.error("Acceptor run error: ", e);
        } finally {
            if (ssc != null) {
                try {
                    ssc.close();
                } catch (IOException e) {
                    log.error("close ServerSocketChannel error: ", e);
                }
            }
        }
    }
}
