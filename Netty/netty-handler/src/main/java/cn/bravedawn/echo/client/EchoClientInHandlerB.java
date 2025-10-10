package cn.bravedawn.echo.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author : depers
 * @Date : Created in 2025-10-10 16:02
 */
@Slf4j
public class EchoClientInHandlerB extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("B->客户端接收消息开始");
        ctx.fireChannelRead(msg);
        log.info("B->客户端接收消息结束");
    }
}
