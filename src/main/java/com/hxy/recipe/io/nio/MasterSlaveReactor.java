package com.hxy.recipe.io.nio;

import com.hxy.recipe.util.Utils;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MasterSlaveReactor {

    public static class Reactor implements Runnable {

        private final Selector selector;
        private final ServerSocketChannel serverSocketChannel;
        private final List<EventLoop> eventLoopList = new ArrayList<>(Utils.CORES);

        public Reactor(int port) throws IOException {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.configureBlocking(false);
            this.serverSocketChannel.bind(new InetSocketAddress(port));
            SelectionKey selectionKey = this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(new Accepter(serverSocketChannel, eventLoopList));
        }

        @Override
        public void run() {
            log.info("server start");
            for (int i = 0; i < Utils.CORES; i++) {
                EventLoop eventLoop = new EventLoop();
                eventLoop.start();
                this.eventLoopList.add(eventLoop);
            }

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
                    log.error("IOException: {}", e);
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
        private final AtomicInteger idx = new AtomicInteger(0);

        private final ServerSocketChannel serverSocketChannel;
        private final List<EventLoop> eventLoopList;

        public Accepter(ServerSocketChannel serverSocketChannel, List<EventLoop> eventLoopList) {
            this.serverSocketChannel = serverSocketChannel;
            this.eventLoopList = eventLoopList;
        }

        @Override
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                log.info("new connection");
                socketChannel.configureBlocking(false);

                Selector selector = eventLoopList.get(idx.getAndIncrement() % eventLoopList.size()).getSelector();
                SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                selectionKey.attach(new Handler(socketChannel));
                selector.wakeup();
            } catch (IOException e) {
                log.error("IOException: {}", e);
            }
        }
    }

    public static class Handler implements Runnable {

        private final SocketChannel socketChannel;

        public Handler(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(128);
                int readBytes = socketChannel.read(buffer);
                log.info("read {} bytes from port {}", readBytes, socketChannel.getRemoteAddress());

                if (readBytes == -1) {
                    socketChannel.close();
                    log.info("connection close");
                    return;
                }

                if (readBytes <= 0) {
                    throw new RuntimeException("no read data");
                }

                Try.run(() -> {
                    buffer.flip();

                    byte[] bytes = new byte[readBytes];
                    buffer.get(bytes);

                    String input = new String(bytes);
                    String output = process(input);

                    ByteBuffer writeBuffer = ByteBuffer.wrap(output.getBytes());
                    int writeBytes = socketChannel.write(writeBuffer);
                    log.info("write {} bytes to port {}", writeBytes, socketChannel.getRemoteAddress());
                });
            } catch (IOException e) {
                log.error("IOException: {}", e);
            }
        }

        // biz process
        private String process(String input) {
            log.info("recv input: {}", input);
            return input;
        }
    }

    public static class EventLoop extends Thread {

        @Getter
        private final Selector selector;

        public EventLoop() {
            this.selector = Try.of(Selector::open).get();
        }

        @Override
        public void run() {
            log.info("event loop start");
            while (!Thread.interrupted()) {
                try {
                    this.selector.select();
                    Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey selectionKey = it.next();
                        it.remove();

                        Object attachment = selectionKey.attachment();
                        Runnable runnable = (Runnable) attachment;
                        runnable.run();
                    }
                } catch (IOException e) {
                    log.error("IOException: {}", e);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // sumCost: 6785 seconds, totalCost: 2703 milliseconds
        new Reactor(Utils.PORT).run();
    }

}
