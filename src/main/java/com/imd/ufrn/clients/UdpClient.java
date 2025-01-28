package com.imd.ufrn.clients;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClient implements Client {

    @Override
    public String sendRequest(String request, int port) {
        try (DatagramSocket socket = new DatagramSocket()) {

            socket.setSoTimeout(3000);

            InetAddress localAddress = InetAddress.getByName("localhost");

            byte[] buf = request.getBytes();

            DatagramPacket packet = new DatagramPacket(buf, buf.length, localAddress, port);
            socket.send(packet);


            byte[] buffer = new byte[1024];

            DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);

            socket.receive(receivedPacket);

            String response = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

            return response;
        }catch (Exception e) {
            return null;
        }
    }

}
