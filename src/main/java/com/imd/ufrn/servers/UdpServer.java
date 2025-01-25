package com.imd.ufrn.servers;

import com.imd.ufrn.handlers.UdpHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer extends Server{


    public UdpServer() {
    }

    public UdpServer(Integer port) {
        super(port);
    }

    public void start() {

        try(DatagramSocket socket = new DatagramSocket(port)) {
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
