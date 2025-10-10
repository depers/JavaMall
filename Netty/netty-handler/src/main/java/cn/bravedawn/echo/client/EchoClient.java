package cn.bravedawn.echo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author : depers
 * @Date : Created in 2025-10-10 14:40
 */
@Slf4j
public class EchoClient {

    private static final String host = System.getProperty("host", "127.0.0.1");
    private static final int port = Integer.parseInt(System.getProperty("port", "8007"));
    private static final int size = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            // 先写出的，逆序执行
                            p.addLast(new EchoClientOutHandlerA());
                            p.addLast(new EchoClientOutHandlerB());
                            p.addLast(new EchoClientOutHandlerC());

                            // 后写入的，顺序执行
                            p.addLast(new EchoClientInHandlerA());
                            p.addLast(new EchoClientInHandlerB());
                            p.addLast(new EchoClientInHandlerC());
                        }
                    });

            ChannelFuture f = bootstrap.connect(host, port).sync();


            f.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    log.info("连接已关闭");
                }
            }).sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
