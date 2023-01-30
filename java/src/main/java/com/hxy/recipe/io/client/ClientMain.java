package com.hxy.recipe.io.client;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * a simple client send msg and record rtt
 */
@Slf4j
public class ClientMain {

    public static class Client implements Callable<Long> {

        private final int port;
        private final long start;

        public Client(int port) {
            this.port = port;
            this.start = System.currentTimeMillis();
        }

        @Override
        public Long call() {
            Socket socket;
            try {
                socket = new Socket("localhost", port);
                log.info("client connect");
            } catch (IOException e) {
                log.error("IOException: {}", e);
                return 0L;
            }

            try (PrintWriter pw = new PrintWriter(socket.getOutputStream(), true); BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                log.info("client send");
                pw.println("hello world");

                String line = br.readLine();
                log.info("client recv: " + line);
            } catch (IOException e) {
                log.error("IOException: {}", e);
                return 0L;
            } finally {
                try {
                    socket.close();
                    log.info("client end");
                } catch (IOException e) {
                    log.error("IOException: {}", e);
                }
            }

            return System.currentTimeMillis() - start;
        }
    }

    private static void run(int port) throws Exception {
        long start = System.currentTimeMillis();
        final int requests = 1000;
        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < requests; i++) {
            Future<Long> future = Utils.newExecutors("client").submit(new Client(port));
            futures.add(future);
        }

        long sumCost = 0L;
        for (Future<Long> future : futures) {
            long perCost = future.get();
            sumCost += perCost;
        }

        long totalCost = System.currentTimeMillis() - start;
        log.info("sumCost: {} seconds, totalCost: {} milliseconds", TimeUnit.MILLISECONDS.toSeconds(sumCost), totalCost);
    }

    public static void main(String[] args) throws Exception {
        run(Utils.PORT);
        // run(Utils.PORT + 1);
    }

}
