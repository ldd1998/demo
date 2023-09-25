package org.example.service.forward;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 可行，但是要重新开一个端口
 */

//@Component
//@EnableAsync
public class DatabaseConnectionProxy {
    private int proxyPort = 8082; // 代理服务器的端口
    private String databaseHost = "192.168.1.107";
    private int databasePort = 3306; // 数据库服务器的端口

    @Scheduled(initialDelay = 1000L,fixedDelay = Integer.MAX_VALUE)
    @Async
    public void startProxy() {
        try (ServerSocket serverSocket = new ServerSocket(proxyPort)) {
            System.out.println("等待客户端连接...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("客户端连接成功");

                // 连接到数据库服务器
                Socket databaseSocket = new Socket(databaseHost, databasePort);

                // 创建新线程来处理客户端请求并将数据转发到数据库服务器
                Thread clientThread = new Thread(new Forwarder(clientSocket, databaseSocket));
                clientThread.start();

                // 创建新线程来处理数据库服务器响应并将数据返回给客户端
                Thread databaseThread = new Thread(new Forwarder(databaseSocket, clientSocket));
                databaseThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Forwarder implements Runnable {
        private Socket sourceSocket;
        private Socket targetSocket;

        public Forwarder(Socket sourceSocket, Socket targetSocket) {
            this.sourceSocket = sourceSocket;
            this.targetSocket = targetSocket;
        }

        @Override
        public void run() {
            try (
                InputStream sourceInput = sourceSocket.getInputStream();
                OutputStream sourceOutput = sourceSocket.getOutputStream();
                InputStream targetInput = targetSocket.getInputStream();
                OutputStream targetOutput = targetSocket.getOutputStream()
            ) {
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = sourceInput.read(buffer)) != -1) {
                    targetOutput.write(buffer, 0, bytesRead);
                    targetOutput.flush();
                }
            } catch (IOException e) {
//                e.printStackTrace();
            } finally {
                try {
                    sourceSocket.close();
                    targetSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
