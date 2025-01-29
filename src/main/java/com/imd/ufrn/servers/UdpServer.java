package com.imd.ufrn.servers;

import com.imd.ufrn.clients.Client;
import com.imd.ufrn.clients.UdpClient;
import com.imd.ufrn.handlers.UdpHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class UdpServer extends Server {

    private Logger logger = Logger.getLogger(UdpServer.class.getName());

    public UdpServer() {
    }

    public UdpServer(Integer port) {
        super(port);
    }

    public void start() {

        logger.info("Registering...");
        register();

        logger.info("\u001B[34mStarting UDP Server\u001B[0m");

        try (DatagramSocket socket = new DatagramSocket(port)) {
            logger.info("\u001B[34mUDP Server started\u001B[0m");

            while (true) {

                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                socket.receive(packet);

                executor.execute(new UdpHandler(socket, packet));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void register() {
        boolean isRegistered = false;

        while (!isRegistered) {
            Client client = new UdpClient();

            String serverPort = String.valueOf(this.port);

            String request = "/register;/tickets;" + serverPort;

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