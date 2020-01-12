package com.hxy.recipe.io.bio;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class ThreadPerConnectionServer {

    public static class Server implements Runnable {

        private enum Strategy {
            new_thread,
            single_thread,
            thread_pool
        }

        private final Strategy strategy;
        private final ServerSocket serverSocket;

        public Server(Strategy strategy, int port) throws IOException {
            this.strategy = strategy;
            this.serverSocket = new ServerSocket(port);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                        log.info("server socket close");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }

        @Override
        public void run() {
            log.info("server start");
            while (!Thread.interrupted()) {
                try {
                    // new thread or single thread or thread pool
                    Socket socket = serverSocket.accept();
                    log.info("new connection");

                    Handler handler = new Handler(socket);
                    if (strategy == Strategy.single_thread) {
                        handler.run();
                    } else if (strategy == Strategy.new_thread) {
                        new Thread(handler).start();
                    } else if (strategy == Strategy.thread_pool) {
                        Utils.newExecutors("thread-per-connection").execute(handler);
                    } else {
                        log.error("no strategy");
                    }
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
            try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {
                String line;
                while ((line = br.readLine()) != null) {
                    log.info("server recv {} from port {}", line, socket.getPort());
                    pw.println(line);
                }

                log.info("server process end");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    log.info("connection close");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // sumCost: 5854 seconds, totalCost: 1874 milliseconds
        new Thread(new Server(Server.Strategy.single_thread, Utils.PORT)).start();

        // sumCost: 3026 seconds, totalCost: 1174 milliseconds
        // new Thread(new Server(Server.Strategy.new_thread, Utils.PORT)).start();

        // sumCost: 1505 seconds, totalCost: 535 milliseconds
        // new Thread(new Server(Server.Strategy.thread_pool, Utils.PORT)).start();
    }

}
