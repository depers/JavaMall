package cn.bravedawn.network.socket.bio.v4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

/**
 * @Author : depers
 * @Date : Created in 2025-09-07 10:07
 */
public class LengthBasedSocketServer {


    private static final int PORT = 8080;
    private static final int HEADER_LENGTH = 4; // 长度字段占4字节

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("服务器启动，监听端口: " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     InputStream input = clientSocket.getInputStream();
                     OutputStream output = clientSocket.getOutputStream()) {

                    System.out.println("客户端连接: " + clientSocket.getRemoteSocketAddress());

                    // 处理客户端请求
                    handleClientRequest(input, output);

                } catch (IOException e) {
                    System.out.println("客户端连接处理异常: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("服务器启动失败: " + e.getMessage());
        }
    }

    private static void handleClientRequest(InputStream input, OutputStream output) throws IOException {
        while (true) {
            try {
                // 1. 读取4字节的长度字段
                byte[] lengthBytes = readExactly(input, HEADER_LENGTH);
                if (lengthBytes == null) {
                    System.out.println("客户端断开连接");
                    break;
                }

                // 2. 将4字节转换为整数（大端序，可根据协议调整）
                int messageLength = ByteBuffer.wrap(lengthBytes).getInt();

                // 验证消息长度合理性
                if (messageLength <= 0 || messageLength > 1024 * 1024) { // 限制最大1MB
                    System.out.println("无效的消息长度: " + messageLength);
                    sendErrorResponse(output, "Invalid message length");
                    break;
                }

                // 3. 读取实际的消息内容
                byte[] messageBytes = readExactly(input, messageLength);
                if (messageBytes == null) {
                    System.out.println("读取消息内容时客户端断开连接");
                    break;
                }

                // 4. 处理消息
                String message = new String(messageBytes, "UTF-8");
                System.out.println("收到消息，长度: " + messageLength + ", 内容: " + message);

                // 5. 构造响应（同样包含4字节长度头）
                String response = "服务器已收到: " + message;
                byte[] responseBytes = response.getBytes("UTF-8");

                // 响应格式：4字节长度 + 实际内容
                ByteBuffer responseBuffer = ByteBuffer.allocate(HEADER_LENGTH + responseBytes.length);
                responseBuffer.putInt(responseBytes.length); // 写入长度头
                responseBuffer.put(responseBytes);           // 写入实际内容

                output.write(responseBuffer.array());
                output.flush();

            } catch (SocketException e) {
                System.out.println("客户端异常断开: " + e.getMessage());
                break;
            } catch (IOException e) {
                System.out.println("IO异常: " + e.getMessage());
                break;
            }
        }
    }

    /**
     * 精确读取指定长度的字节
     */
    private static byte[] readExactly(InputStream input, int length) throws IOException {
        byte[] buffer = new byte[length];
        int totalRead = 0;

        while (totalRead < length) {
            int bytesRead = input.read(buffer, totalRead, length - totalRead);
            if (bytesRead == -1) {
                return null; // 客户端断开连接
            }
            totalRead += bytesRead;
        }
        return buffer;
    }

    /**
     * 发送错误响应
     */
    private static void sendErrorResponse(OutputStream output, String errorMessage) throws IOException {
        byte[] errorBytes = errorMessage.getBytes("UTF-8");
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_LENGTH + errorBytes.length);
        buffer.putInt(errorBytes.length);
        buffer.put(errorBytes);
        output.write(buffer.array());
        output.flush();
    }
}
