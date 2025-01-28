package com.imd.ufrn.servers;

import com.imd.ufrn.clients.Client;
import com.imd.ufrn.clients.TcpClient;
import com.imd.ufrn.clients.UdpClient;
import com.imd.ufrn.handlers.TcpHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class TcpServer extends Server {

    private Logger logger = Logger.getLogger(TcpServer.class.getName());

    public TcpServer() {
    }

    public TcpServer(Integer port) {
        super(port);
    }

    @Override
    public void start() {

        logger.info("Registering...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                register();
            }
        }).start();

        logger.info("\u001B[34mStarting TCP Server\u001B[0m");

        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        logger.info("\u001B[34mTCP Server started\u001B[0m");

        while (true) {
            try {
                Socket socket = server.accept();

                String clientAddress = socket.getInetAddress().getHostAddress();
                int clientPort = socket.getPort();

                logger.info("\u001B[32mConnection established with client: " + clientAddress + ":" + clientPort + "\u001B[0m");

                executor.execute(new TcpHandler(socket));

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        try {
            if (server != null && !server.isClosed()) {
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register() {
        boolean isRegistered = false;

        while (!isRegistered) {
            Client client = new TcpClient();

            String serverPort = String.valueOf(this.port);

            String request = "/register;" + serverPort;

            String response = null;

            try {
                response = client.sendRequest(request, InetAddress.getByName("localhost"),8080);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

            if (response.equals("REGISTERED")) {
                isRegistered = true;
            } else {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
