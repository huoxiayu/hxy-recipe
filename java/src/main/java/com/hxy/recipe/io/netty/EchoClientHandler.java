package com.hxy.recipe.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private static final int TIMES = 5;
    private static final ByteBuf HELLO_MSG = Unpooled.wrappedBuffer("hello netty".getBytes());

    private int current = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(HELLO_MSG);
        log.info("write msg on active");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("read msg {}", msg);
        current++;
        if (current >= TIMES) {
            return;
        }

        ctx.write(msg);
        log.info("write msg {}", msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("caught exception {}", cause);
        ctx.close();
    }

}
