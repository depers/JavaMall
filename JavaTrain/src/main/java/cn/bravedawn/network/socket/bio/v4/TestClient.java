package cn.bravedawn.network.socket.bio.v4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * @Author : depers
 * @Date : Created in 2025-09-07 10:09
 */
public class TestClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             OutputStream output = socket.getOutputStream();
             InputStream input = socket.getInputStream()) {

            String message = "Hello, Server! 这是一个测试消息";
            byte[] messageBytes = message.getBytes("UTF-8");

            // 构造请求：4字节长度 + 实际内容
            ByteBuffer requestBuffer = ByteBuffer.allocate(4 + messageBytes.length);
            requestBuffer.putInt(messageBytes.length);
            requestBuffer.put(messageBytes);

            // 发送请求
            output.write(requestBuffer.array());
            output.flush();

            // 读取响应
            byte[] lengthBytes = new byte[4];
            if (input.read(lengthBytes) != 4) {
                System.out.println("读取响应长度失败");
                return;
            }

            int responseLength = ByteBuffer.wrap(lengthBytes).getInt();
            byte[] responseBytes = new byte[responseLength];
            if (input.read(responseBytes) != responseLength) {
                System.out.println("读取响应内容失败");
                return;
            }

            String response = new String(responseBytes, "UTF-8");
            System.out.println("服务器响应: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
