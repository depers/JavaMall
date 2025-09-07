package cn.bravedawn.network.socket.bio.v5;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.concurrent.*;

/**
 * @Author : depers
 * @Date : Created in 2025-09-07 10:52
 */


public class MultiThreadedSocketServer {
    private static final int PORT = 8080;
    private static final int HEADER_LENGTH = 4;
    private static final int MAX_MESSAGE_SIZE = 1024 * 1024; // 最大1MB
    private static final int THREAD_POOL_SIZE = 10;
    private static final int MAX_QUEUE_SIZE = 100;

    private static volatile boolean isRunning = true;
    private static ExecutorService threadPool;

    public static void main(String[] args) {
        // 创建线程池
        threadPool = new ThreadPoolExecutor(
                THREAD_POOL_SIZE,
                THREAD_POOL_SIZE,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(MAX_QUEUE_SIZE),
                new ThreadPoolExecutor.CallerRunsPolicy() // 队列满时由主线程处理
        );

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("多线程服务器启动，监听端口: " + PORT);
            System.out.println("线程池大小: " + THREAD_POOL_SIZE);

            // 添加关闭钩子
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                shutdownServer();
            }));

            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("接受客户端连接: " + clientSocket.getRemoteSocketAddress());

                    // 提交任务到线程池
                    threadPool.execute(new ClientHandler(clientSocket));

                } catch (SocketException e) {
                    if (isRunning) {
                        System.out.println("服务器Socket异常: " + e.getMessage());
                    }
                } catch (IOException e) {
                    System.out.println("接受连接异常: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("服务器启动失败: " + e.getMessage());
        } finally {
            shutdownServer();
        }
    }

    private static void shutdownServer() {
        isRunning = false;
        if (threadPool != null) {
            System.out.println("正在关闭线程池...");
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("服务器已关闭");
    }

    /**
     * 客户端处理线程
     */
    static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            String clientAddress = clientSocket.getRemoteSocketAddress().toString();
            System.out.println("线程[" + Thread.currentThread().getName() + "] 开始处理客户端: " + clientAddress);

            try (InputStream input = clientSocket.getInputStream();
                 OutputStream output = clientSocket.getOutputStream()) {

                handleClientCommunication(input, output, clientAddress);

            } catch (IOException e) {
                System.out.println("客户端 " + clientAddress + " IO异常: " + e.getMessage());
            } finally {
                try {
                    if (!clientSocket.isClosed()) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    System.out.println("关闭客户端Socket异常: " + e.getMessage());
                }
                System.out.println("客户端 " + clientAddress + " 连接已关闭");
            }
        }

        private void handleClientCommunication(InputStream input, OutputStream output, String clientAddress) {
            while (isRunning && !clientSocket.isClosed()) {
                try {
                    // 1. 读取长度头
                    byte[] lengthBytes = readExactly(input, HEADER_LENGTH);
                    if (lengthBytes == null) {
                        System.out.println("客户端 " + clientAddress + " 断开连接");
                        break;
                    }

                    // 2. 解析消息长度
                    int messageLength = ByteBuffer.wrap(lengthBytes).getInt();

                    // 验证长度
                    if (messageLength <= 0 || messageLength > MAX_MESSAGE_SIZE) {
                        System.out.println("客户端 " + clientAddress + " 发送了无效的消息长度: " + messageLength);
                        sendErrorResponse(output, "Invalid message length");
                        break;
                    }

                    // 3. 读取消息内容
                    byte[] messageBytes = readExactly(input, messageLength);
                    if (messageBytes == null) {
                        System.out.println("读取消息内容时客户端 " + clientAddress + " 断开连接");
                        break;
                    }

                    // 4. 处理消息
                    processMessage(messageBytes, output, clientAddress);

                } catch (SocketTimeoutException e) {
                    System.out.println("客户端 " + clientAddress + " 读取超时");
                    break;
                } catch (IOException e) {
                    System.out.println("客户端 " + clientAddress + " 通信异常: " + e.getMessage());
                    break;
                }
            }
        }

        private void processMessage(byte[] messageBytes, OutputStream output, String clientAddress) throws IOException {
            String message = new String(messageBytes, "UTF-8");
            System.out.println("收到来自 " + clientAddress + " 的消息，长度: " + messageBytes.length + ", 内容: " + message);

            // 模拟业务处理（可以替换为实际业务逻辑）
            try {
                Thread.sleep(100); // 模拟处理耗时
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // 构造响应
            String response = "服务器[" + Thread.currentThread().getName() + "]已处理: " + message;
            byte[] responseBytes = response.getBytes("UTF-8");

            ByteBuffer responseBuffer = ByteBuffer.allocate(HEADER_LENGTH + responseBytes.length);
            responseBuffer.putInt(responseBytes.length);
            responseBuffer.put(responseBytes);

            output.write(responseBuffer.array());
            output.flush();

            System.out.println("已向客户端 " + clientAddress + " 发送响应");
        }

        private byte[] readExactly(InputStream input, int length) throws IOException {
            byte[] buffer = new byte[length];
            int totalRead = 0;

            while (totalRead < length && isRunning) {
                int bytesRead = input.read(buffer, totalRead, length - totalRead);
                if (bytesRead == -1) {
                    return null; // 客户端断开连接
                }
                totalRead += bytesRead;
            }
            return buffer;
        }

        private void sendErrorResponse(OutputStream output, String errorMessage) throws IOException {
            byte[] errorBytes = errorMessage.getBytes("UTF-8");
            ByteBuffer buffer = ByteBuffer.allocate(HEADER_LENGTH + errorBytes.length);
            buffer.putInt(errorBytes.length);
            buffer.put(errorBytes);
            output.write(buffer.array());
            output.flush();
        }
    }
}
