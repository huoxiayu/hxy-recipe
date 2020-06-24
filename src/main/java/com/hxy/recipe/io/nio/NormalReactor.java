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
        private final ServerSocketChannel serverSocketChannel;

        public Reactor(Strategy strategy, int port) throws IOException {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.configureBlocking(false);
            this.serverSocketChannel.bind(new InetSocketAddress(port));
            SelectionKey selectionKey = this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(new Accepter(strategy, serverSocketChannel, selector));
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
                            Utils.sleepInSeconds(3L);
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
        private final ServerSocketChannel serverSocketChannel;
        private final Selector selector;

        public Accepter(Strategy strategy, ServerSocketChannel serverSocketChannel, Selector selector) {
            this.strategy = strategy;
            this.serverSocketChannel = serverSocketChannel;
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                log.info("new connection");
                socketChannel.configureBlocking(false);
                SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                selectionKey.attach(new Handler(strategy, selectionKey, socketChannel));
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
        private SocketChannel socketChannel;
        private int state;
        private ByteBuffer readBuffer;
        private String output;

        public Handler(Strategy strategy, SelectionKey selectionKey, SocketChannel socketChannel) {
            this.strategy = strategy;
            this.selectionKey = selectionKey;
            this.socketChannel = socketChannel;
            this.state = READING;
            this.readBuffer = ByteBuffer.allocate(128);
        }

        @Override
        public void run() {
            try {
                if (state == READING) {
                    int readBytes = 0;
                    for (; ; ) {
                        int read = socketChannel.read(readBuffer);
                        if (read > 0) {
                            readBytes += read;
                        } else {
                            break;
                        }
                    }
                    log.info("read {} bytes from port {}", readBytes, socketChannel.getRemoteAddress());

                    if (readBytes == -1) {
                        selectionKey.cancel();
                        socketChannel.close();
                        log.info("connection close here");
                        return;
                    }

                    if (readBytes == 0) {
                        log.info("socketChannel.isOpen(): {}", socketChannel.isOpen());
                        log.info("socketChannel.isConnected(): {}", socketChannel.isConnected());
                        log.info("selectionKey.isValid(): {}", selectionKey.isValid());
                        log.info("read empty");
                        Utils.sleepInSeconds(5L);
                        return;
                    }

                    readBuffer.flip();
                    byte[] bytes = new byte[readBytes];
                    readBuffer.get(bytes);
                    readBuffer.clear();
                    String input = new String(bytes);

                    Runnable runnable = () -> Try.run(() -> {
                        output = process(input);
                        state = WRITING;

                        int interestOps = selectionKey.interestOps();
                        int newInterestOps = interestOps | SelectionKey.OP_WRITE;
                        selectionKey.interestOps(newInterestOps);
                    });
                    if (strategy == Strategy.single_thread) {
                        runnable.run();
                    } else {
                        Utils.newExecutors("process-").execute(runnable);
                    }
                } else if (state == WRITING) {
                    byte[] outputBytes = output.getBytes();
                    ByteBuffer writeBuffer = ByteBuffer.allocate(outputBytes.length);
                    writeBuffer.put(outputBytes);
                    writeBuffer.flip();
                    socketChannel.write(writeBuffer);
                    log.info("server write end");

                    int interestOps = selectionKey.interestOps();
                    int newInterestOps = interestOps & (~SelectionKey.OP_WRITE);
                    selectionKey.interestOps(newInterestOps);

                    state = READING;
                }
            } catch (IOException e) {
                log.error("IOException: {}", e);
                selectionKey.cancel();
                try {
                    socketChannel.close();
                    log.info("connection close here");
                } catch (IOException e1) {
                    log.info("connection close fail for {}", e1);
                }
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
        new Reactor(Strategy.single_thread, Utils.PORT).run();

        // sumCost: 7429 seconds, totalCost: 2893 milliseconds
        new Reactor(Strategy.multi_thread, Utils.PORT + 1).run();
    }

}
