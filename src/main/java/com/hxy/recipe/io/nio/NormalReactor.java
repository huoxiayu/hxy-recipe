package com.hxy.recipe.io.nio;

import com.hxy.recipe.util.Utils;
import io.vavr.control.Try;
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

    public enum Strategy {
        single_thread,
        multi_thread
    }

    public static class Reactor implements Runnable {

        private final Selector selector;

        public Reactor(Strategy strategy, int port) throws IOException {
            this.selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            SelectionKey acceptSelectionKey = serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
            acceptSelectionKey.attach(new Accepter(strategy, acceptSelectionKey));
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
                        SelectionKey sk = it.next();
                        boolean acceptable = sk.isAcceptable();
                        boolean readable = sk.isReadable();
                        boolean writable = sk.isWritable();
                        log.info("trigger event acceptable {}, readable {}, writable {}", acceptable, readable, writable);
                        it.remove();
                        Object attachment = sk.attachment();
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
                Runnable runnable = (Runnable) attachment;
                runnable.run();
            } else {
                log.error("can not dispatch: {}", attachment.getClass().getSimpleName());
            }
        }
    }

    public static class Accepter implements Runnable {

        private final Strategy strategy;
        private final SelectionKey acceptSelectionKey;

        public Accepter(Strategy strategy, SelectionKey acceptSelectionKey) {
            this.strategy = strategy;
            this.acceptSelectionKey = acceptSelectionKey;
        }

        @Override
        public void run() {
            try {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) acceptSelectionKey.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                log.info("new connection");
                socketChannel.configureBlocking(false);
                Selector selector = acceptSelectionKey.selector();
                SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                selectionKey.attach(new Handler(strategy, selectionKey));
                selector.wakeup();
            } catch (IOException e) {
                log.error("IOException: {}", e);
            }
        }
    }

    public static class Handler implements Runnable {

        private static final int READING = 1;
        private static final int WRITING = 2;

        private Strategy strategy;
        private SelectionKey selectionKey;
        private int state;
        private String output;

        public Handler(Strategy strategy, SelectionKey selectionKey) {
            this.strategy = strategy;
            this.selectionKey = selectionKey;
            this.state = READING;
        }

        @Override
        public void run() {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            try {
                if (state == READING) {
                    log.info("reading");
                    ByteBuffer readBuffer = ByteBuffer.allocate(128);
                    int readBytes = socketChannel.read(readBuffer);
                    log.info("read {} bytes from port {}", readBytes, socketChannel.getRemoteAddress());

                    if (readBytes == -1) {
                        socketChannel.close();
                        log.info("connection close here");
                        return;
                    }

                    if (readBytes <= 0) {
                        throw new RuntimeException("no data read");
                    }

                    Runnable runnable = () -> Try.run(() -> {
                        readBuffer.flip();

                        byte[] bytes = new byte[readBytes];
                        readBuffer.get(bytes);

                        String input = new String(bytes);
                        output = process(input);

                        state = WRITING;
                        selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
                        log.info("register op write");
                        selectionKey.selector().wakeup();
                    });

                    if (strategy == Strategy.single_thread) {
                        runnable.run();
                    } else {
                        Utils.newExecutors("process-").execute(runnable);
                    }
                } else if (state == WRITING) {
                    log.info("writing");
                    ByteBuffer writeBuffer = ByteBuffer.wrap(output.getBytes());
                    int writeBytes = socketChannel.write(writeBuffer);
                    log.info("write {} bytes to port {}", writeBytes, socketChannel.getRemoteAddress());

                    state = READING;
                    selectionKey.interestOps(selectionKey.interestOps() & (~SelectionKey.OP_WRITE));
                    log.info("unregister op write");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // biz process
        private String process(String input) {
            log.info("recv input: {}", input);
            return input;
        }
    }

    public static void main(String[] args) throws IOException {
        // sumCost: 145854 seconds, totalCost: 58106 milliseconds
        Utils.newExecutors("reactor").execute(new Reactor(Strategy.single_thread, Utils.PORT));

        // sumCost: 7429 seconds, totalCost: 2893 milliseconds
        Utils.newExecutors("reactor").execute(new Reactor(Strategy.multi_thread, Utils.PORT + 1));

        Utils.join();
    }

}
