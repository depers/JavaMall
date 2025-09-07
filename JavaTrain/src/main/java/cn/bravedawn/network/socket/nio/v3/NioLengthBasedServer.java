package cn.bravedawn.network.socket.nio.v3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author : depers
 * @Date : Created in 2025-09-07 13:28
 */

public class NioLengthBasedServer {
    private static final int PORT = 8080;
    private static final int HEADER_LENGTH = 4;
    private static final int MAX_MESSAGE_SIZE = 1024 * 1024; // 最大1MB
    private static final int BUFFER_SIZE = 1024;

    private Selector selector;
    private ServerSocketChannel serverChannel;
    private ExecutorService businessExecutor;

    // 存储每个连接的会话状态
    private final ConcurrentHashMap<SelectionKey, ClientSession> sessionMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        NioLengthBasedServer server = new NioLengthBasedServer();
        server.start();
    }

    public void start() {
        try {
            // 初始化线程池
            businessExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

            // 初始化Selector和ServerSocketChannel
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(PORT));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("NIO服务器启动，监听端口: " + PORT);

            // 事件循环
            eventLoop();

        } catch (IOException e) {
            System.out.println("服务器启动失败: " + e.getMessage());
        } finally {
            shutdown();
        }
    }

    private void eventLoop() {
        while (true) {
            try {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    try {
                        if (key.isAcceptable()) {
                            handleAccept(key);
                        } else if (key.isReadable()) {
                            handleRead(key);
                        }
                    } catch (IOException e) {
                        System.out.println("处理事件异常: " + e.getMessage());
                        closeClient(key);
                    }
                }
            } catch (IOException e) {
                System.out.println("Selector异常: " + e.getMessage());
                break;
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);

        // 注册读事件
        SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);

        // 创建会话状态
        ClientSession session = new ClientSession(clientChannel);
        sessionMap.put(clientKey, session);

        System.out.println("接受客户端连接: " + clientChannel.getRemoteAddress());
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ClientSession session = sessionMap.get(key);

        if (session == null) {
            closeClient(key);
            return;
        }

        // 读取数据到会话缓冲区
        int bytesRead = channel.read(session.readBuffer);

        if (bytesRead == -1) {
            System.out.println("客户端断开连接: " + channel.getRemoteAddress());
            closeClient(key);
            return;
        }

        if (bytesRead > 0) {
            // 处理接收到的数据
            processReceivedData(session, key);
        }
    }

    private void processReceivedData(ClientSession session, SelectionKey key) {
        session.readBuffer.flip(); // 切换为读模式

        try {
            while (session.readBuffer.remaining() > 0) {
                // 如果还没有读取长度头
                if (session.expectedLength == -1) {
                    if (session.readBuffer.remaining() < HEADER_LENGTH) {
                        // 数据不足，等待更多数据
                        break;
                    }

                    // 读取长度头
                    session.expectedLength = session.readBuffer.getInt();

                    // 验证长度
                    if (session.expectedLength <= 0 || session.expectedLength > MAX_MESSAGE_SIZE) {
                        System.out.println("无效的消息长度: " + session.expectedLength);
                        sendErrorResponse(session.channel, "Invalid message length");
                        closeClient(key);
                        return;
                    }

                    // 准备消息缓冲区
                    session.messageBuffer = ByteBuffer.allocate(session.expectedLength);
                }

                // 读取消息内容
                if (session.messageBuffer != null) {
                    int bytesToRead = Math.min(session.readBuffer.remaining(),
                            session.messageBuffer.remaining());

                    byte[] temp = new byte[bytesToRead];
                    session.readBuffer.get(temp);
                    session.messageBuffer.put(temp);

                    // 检查是否读完完整消息
                    if (!session.messageBuffer.hasRemaining()) {
                        // 消息读取完成，提交业务处理
                        session.messageBuffer.flip();
                        byte[] messageBytes = new byte[session.expectedLength];
                        session.messageBuffer.get(messageBytes);

                        // 提交到业务线程池处理
                        businessExecutor.execute(() -> {
                            processCompleteMessage(messageBytes, session.channel, key);
                        });

                        // 重置会话状态
                        session.reset();
                    }
                }
            }
        } finally {
            // 压缩缓冲区，准备下一次读取
            session.readBuffer.compact();
        }
    }

    private void processCompleteMessage(byte[] messageBytes, SocketChannel channel, SelectionKey key) {
        try {
            String message = new String(messageBytes, StandardCharsets.UTF_8);
            String clientAddress = channel.getRemoteAddress().toString();

            System.out.println("收到来自 " + clientAddress + " 的消息，长度: " + messageBytes.length);
            System.out.println("消息内容: " + message);

            // 模拟业务处理
            Thread.sleep(100);

            // 构造响应
            String response = "服务器已处理: " + message;
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            ByteBuffer responseBuffer = ByteBuffer.allocate(HEADER_LENGTH + responseBytes.length);
            responseBuffer.putInt(responseBytes.length);
            responseBuffer.put(responseBytes);
            responseBuffer.flip();

            // 发送响应
            while (responseBuffer.hasRemaining()) {
                channel.write(responseBuffer);
            }

            System.out.println("已向客户端发送响应: " + clientAddress);

        } catch (Exception e) {
            System.out.println("处理消息异常: " + e.getMessage());
            closeClient(key);
        }
    }

    private void sendErrorResponse(SocketChannel channel, String errorMessage) {
        try {
            byte[] errorBytes = errorMessage.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.allocate(HEADER_LENGTH + errorBytes.length);
            buffer.putInt(errorBytes.length);
            buffer.put(errorBytes);
            buffer.flip();

            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        } catch (IOException e) {
            System.out.println("发送错误响应失败: " + e.getMessage());
        }
    }

    private void closeClient(SelectionKey key) {
        try {
            if (key != null) {
                key.cancel();
                SocketChannel channel = (SocketChannel) key.channel();
                if (channel != null) {
                    System.out.println("关闭客户端连接: " + channel.getRemoteAddress());
                    channel.close();
                }
            }
        } catch (IOException e) {
            System.out.println("关闭客户端连接异常: " + e.getMessage());
        } finally {
            sessionMap.remove(key);
        }
    }

    private void shutdown() {
        try {
            if (selector != null) {
                selector.close();
            }
            if (serverChannel != null) {
                serverChannel.close();
            }
            if (businessExecutor != null) {
                businessExecutor.shutdown();
            }
            System.out.println("服务器已关闭");
        } catch (IOException e) {
            System.out.println("关闭服务器异常: " + e.getMessage());
        }
    }

    /**
     * 客户端会话状态
     */
    private static class ClientSession {
        private final SocketChannel channel;
        private final ByteBuffer readBuffer;
        private int expectedLength = -1;
        private ByteBuffer messageBuffer;

        public ClientSession(SocketChannel channel) {
            this.channel = channel;
            this.readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        }

        public void reset() {
            expectedLength = -1;
            messageBuffer = null;
        }
    }
}
