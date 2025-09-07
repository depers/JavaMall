package cn.bravedawn.network.socket.bio.v5;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.concurrent.*;

/**
 * @Author : depers
 * @Date : Created in 2025-09-07 10:55
 */

public class MultiThreadTestClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final int CLIENT_COUNT = 5; // 并发客户端数量

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(CLIENT_COUNT);
        CountDownLatch latch = new CountDownLatch(CLIENT_COUNT);

        for (int i = 0; i < CLIENT_COUNT; i++) {
            final int clientId = i + 1;
            executor.execute(() -> {
                try {
                    testClient(clientId);
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        executor.shutdown();
        System.out.println("所有客户端测试完成");
    }

    private static void testClient(int clientId) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             OutputStream output = socket.getOutputStream();
             InputStream input = socket.getInputStream()) {

            socket.setSoTimeout(5000); // 设置超时时间

            String message = "Hello from Client-" + clientId;
            byte[] messageBytes = message.getBytes("UTF-8");

            // 构造请求
            ByteBuffer requestBuffer = ByteBuffer.allocate(4 + messageBytes.length);
            requestBuffer.putInt(messageBytes.length);
            requestBuffer.put(messageBytes);

            // 发送请求
            output.write(requestBuffer.array());
            output.flush();
            System.out.println("客户端-" + clientId + " 发送消息: " + message);

            // 读取响应长度头
            byte[] lengthBytes = new byte[4];
            if (input.read(lengthBytes) != 4) {
                System.out.println("客户端-" + clientId + " 读取响应长度失败");
                return;
            }

            int responseLength = ByteBuffer.wrap(lengthBytes).getInt();
            byte[] responseBytes = new byte[responseLength];

            // 读取响应内容
            int totalRead = 0;
            while (totalRead < responseLength) {
                int bytesRead = input.read(responseBytes, totalRead, responseLength - totalRead);
                if (bytesRead == -1) {
                    System.out.println("客户端-" + clientId + " 读取响应内容失败");
                    return;
                }
                totalRead += bytesRead;
            }

            String response = new String(responseBytes, "UTF-8");
            System.out.println("客户端-" + clientId + " 收到响应: " + response);

        } catch (IOException e) {
            System.out.println("客户端-" + clientId + " 异常: " + e.getMessage());
        }
    }
}
