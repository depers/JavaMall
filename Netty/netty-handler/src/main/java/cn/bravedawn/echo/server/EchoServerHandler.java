package cn.bravedawn.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @Author : depers
 * @Date : Created in 2025-10-10 14:33
 */
@Slf4j
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String req = new String(ByteBufUtil.getBytes((ByteBuf) msg), StandardCharsets.UTF_8);
        log.info("服务器端接收消息, reqMsg={}", req);
        log.info("服务器端发送消息");
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeBytes("你好，客户端".getBytes(StandardCharsets.UTF_8));
        ctx.write(byteBuf);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("出现异常");
        ctx.close();
    }
}
