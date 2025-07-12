package org.example.practice;

import java.io.IOException;

/**
 * @author zhenwu
 * @date 2025/7/6 14:56
 */
public class MultiThreadServer {
    public static void main(String[] args) {
        try {
            Acceptor acceptor = new Acceptor();
            acceptor.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
