package cn.bravedawn.echo.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author : depers
 * @Date : Created in 2025-10-10 16:07
 */

@Slf4j
public class EchoClientOutHandlerB extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        log.info("B->客户端写入数据开始");
        // 这里要用ChannelHandlerContext的write方法，不要用channel的write，否则会走死循环
        // 这里的write需要补充第二个参数，方便调用者感知链路异常
        ctx.write(msg, promise);
        log.info("B->客户端写入数据结束");
    }
}
