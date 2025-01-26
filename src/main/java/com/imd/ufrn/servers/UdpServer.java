package com.imd.ufrn.servers;

import com.imd.ufrn.handlers.UdpHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Logger;

public class UdpServer extends Server{

    private Logger logger = Logger.getLogger(UdpServer.class.getName());

    public UdpServer() {
    }

    public UdpServer(Integer port) {
        super(port);
    }

    public void start() {

        logger.info("\u001B[34mStarting UDP Server\u001B[0m");

        try(DatagramSocket socket = new DatagramSocket(port)) {
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

}
