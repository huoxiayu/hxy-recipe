package com.hxy.recipe.io.nio;

import com.hxy.recipe.util.Utils;
import io.vavr.control.Try;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class NormalReactor {


    public static class Reactor implements Runnable {

        private final Selector selector;
        private final ServerSocketChannel serverSocketChannel;

        public Reactor(int port) throws IOException {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.configureBlocking(false);
            this.serverSocketChannel.bind(new InetSocketAddress(port));
            SelectionKey selectionKey = this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(new Accepter(serverSocketChannel, selector));
        }

        @Override
        public void run() {
            log.info("server start");
            while (!Thread.interrupted()) {
                try {
                    this.selector.select();
                    Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey selectionKey = it.next();
                        it.remove();

                        Object attachment = selectionKey.attachment();
                        if (attachment != null) {
                            dispatch(attachment);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void dispatch(Object attachment) {
            if (attachment instanceof Runnable) {
                Runnable handler = (Runnable) attachment;
                handler.run();
            } else {
                log.info("can not dispatch: {}", attachment.getClass().getSimpleName());
            }
        }
    }

    public static class Accepter implements Runnable {

        private final ServerSocketChannel serverSocketChannel;
        private final Selector selector;

        public Accepter(ServerSocketChannel serverSocketChannel, Selector selector) {
            this.serverSocketChannel = serverSocketChannel;
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                // new connection
                log.info("new connection");
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                // register read
                SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                selectionKey.attach(new Handler(socketChannel));
                // wake up for loop read event in next select round
                selector.wakeup();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Handler implements Runnable {

        @Setter
        private static Strategy strategy;

        private enum Strategy {
            single_thread,
            multi_thread
        }

        private final SocketChannel socketChannel;

        public Handler(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(buffer);
                log.info("read {} bytes from port {}", readBytes, socketChannel.getRemoteAddress());

                if (readBytes > 0) {
                    Runnable runnable = () -> Try.run(() -> {
                        buffer.flip();

                        byte[] bytes = new byte[readBytes];
                        buffer.get(bytes);

                        String input = new String(bytes);
                        String output = process(input);

                        byte[] outputBytes = output.getBytes();
                        ByteBuffer writeBuffer = ByteBuffer.allocate(outputBytes.length);
                        writeBuffer.put(outputBytes);
                        writeBuffer.flip();

                        socketChannel.write(writeBuffer);
                        log.info("server process end");
                    }).get();

                    if (strategy == Strategy.single_thread) {
                        runnable.run();
                    } else if (strategy == Strategy.multi_thread) {
                        Utils.newExecutors("process").execute(runnable);
                    } else {
                        log.error("no strategy");
                    }
                } else {
                    socketChannel.close();
                    log.info("connection close");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // biz process
        private String process(String input) {
            log.info("recv input: {}", input);

            // process biz cost 100 millis
            Utils.sleepInMillis(10L);

            return input;
        }
    }

    public static void main(String[] args) throws IOException {
        // sumCost: 145854 seconds, totalCost: 58106 milliseconds
        // Handler.setStrategy(Handler.Strategy.single_thread);

        // sumCost: 7429 seconds, totalCost: 2893 milliseconds
        Handler.setStrategy(Handler.Strategy.multi_thread);
        new Reactor(Utils.PORT).run();
    }

}
