package cn.bravedawn.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @Author : depers
 * @Date : Created in 2025-10-10 14:48
 */
@Slf4j
public class EchoClientInHandlerA extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("A->客户端发送消息");
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeBytes("服务器你好".getBytes(StandardCharsets.UTF_8));
        ChannelFuture channelFuture = ctx.writeAndFlush(byteBuf);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (!channelFuture.isSuccess()) {
                    log.error("发送消息出现异常", channelFuture.cause());
                    channelFuture.channel().close();
                }
            }
        });

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String resp = new String(ByteBufUtil.getBytes((ByteBuf) msg));
        log.info("A->客户端接收消息开始，resp={}", resp);
        ctx.fireChannelRead(msg);
//         int i = 1 / 0;
        log.info("A->客户端接收消息结束");
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        log.error("接收消息出现异常", cause);
//        ctx.close();
//    }
}
