package cn.bravedawn.network.socket.nio.v3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
/**
 * @Author : depers
 * @Date : Created in 2025-09-07 13:32
 */


public class NioTestClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (SocketChannel channel = SocketChannel.open()) {
            channel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            channel.configureBlocking(true); // 使用阻塞模式简化客户端

            String message = "Hello NIO Server! 这是一个测试消息";
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

            // 构造请求：4字节长度 + 实际内容
            ByteBuffer requestBuffer = ByteBuffer.allocate(4 + messageBytes.length);
            requestBuffer.putInt(messageBytes.length);
            requestBuffer.put(messageBytes);
            requestBuffer.flip();

            // 发送请求
            channel.write(requestBuffer);
            System.out.println("发送消息: " + message);

            // 读取响应长度头
            ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
            while (lengthBuffer.hasRemaining()) {
                if (channel.read(lengthBuffer) == -1) {
                    System.out.println("服务器断开连接");
                    return;
                }
            }
            lengthBuffer.flip();
            int responseLength = lengthBuffer.getInt();

            // 读取响应内容
            ByteBuffer responseBuffer = ByteBuffer.allocate(responseLength);
            while (responseBuffer.hasRemaining()) {
                if (channel.read(responseBuffer) == -1) {
                    System.out.println("读取响应内容失败");
                    return;
                }
            }
            responseBuffer.flip();

            byte[] responseBytes = new byte[responseLength];
            responseBuffer.get(responseBytes);
            String response = new String(responseBytes, StandardCharsets.UTF_8);

            System.out.println("服务器响应: " + response);

        } catch (IOException e) {
            System.out.println("客户端异常: " + e.getMessage());
        }
    }
}