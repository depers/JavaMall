package cn.bravedawn.echo.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author : depers
 * @Date : Created in 2025-10-10 16:03
 */

@Slf4j
public class EchoClientInHandlerC extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("C->客户端接收消息开始");
        ctx.fireChannelRead(msg);
        log.info("C->客户端接收消息结束");
        ctx.close();
    }

//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        log.error("接收消息出现异常", cause);
//        ctx.close();
//    }
}
