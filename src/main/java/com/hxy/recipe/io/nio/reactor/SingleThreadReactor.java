package com.hxy.recipe.io.nio.reactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

@Slf4j
public class SingleThreadReactor {

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
            while (!Thread.interrupted()) {
                try {
                    this.selector.select();
                    Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                    for (SelectionKey selectionKey : selectionKeys) {
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
                SocketChannel socketChannel = serverSocketChannel.accept();
                SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                selectionKey.attach(new Handler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Handler implements Runnable {

        @Override
        public void run() {

        }
    }

    public static void main(String[] args) {

    }

}
