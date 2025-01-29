package com.imd.ufrn.servers;

import com.imd.ufrn.clients.Client;
import com.imd.ufrn.clients.HttpClient;
import com.imd.ufrn.clients.TcpClient;
import com.imd.ufrn.handlers.HttpHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class HttpServer extends Server {

    private Logger logger = Logger.getLogger(HttpServer.class.getName());

    public HttpServer() {
    }

    public HttpServer(Integer port) {
        super(port);
    }

    @Override
    public void start() {
        logger.info("\u001B[34mStarting HTTP Server\u001B[0m");

        register();

        ServerSocket server = null;

        try {
            server = new ServerSocket(port);

            logger.info("\u001B[34mHTTP Server started\u001B[0m");

            while (true) {
                Socket socket = server.accept();

                String info = "\u001B[34mHTTP Client connected" + socket.getInetAddress().getHostAddress() + "\u001B[0m";
                logger.info(info);

                executor.execute(new HttpHandler(socket));

            }

        }catch (IOException ioe) {
            logger.warning(ioe.toString());
        }finally {
            if (server != null && !server.isClosed()) {
                try {
                    server.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void register() {
        boolean isRegistered = false;

        while (!isRegistered) {
            Client client = new HttpClient();

            String serverPort = String.valueOf(this.port);

            String request = "post,/register,/tickets;" + serverPort;

            String response = null;

            try {
                response = client.sendRequest(request, InetAddress.getByName("localhost"),8080);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

            System.out.println(response);
            if (response.contains("REGISTERED")) {
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
