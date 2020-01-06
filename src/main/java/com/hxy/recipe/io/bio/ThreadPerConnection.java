package com.hxy.recipe.io.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * bio impl
 */
@Slf4j
public class ThreadPerConnection {

    public static class Server implements Runnable {

        private final ServerSocket serverSocket;

        public Server(int port) throws IOException {
            this.serverSocket = new ServerSocket(port);
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    // new thread or single thread or thread pool
                    new Thread(new Handler(serverSocket.accept())).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static class Handler implements Runnable {

        private final Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            log.info("enter handler");
            try {
                socket.getOutputStream().write(666);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new Server(8090)).start();
    }

}
