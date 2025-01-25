package com.imd.ufrn.handlers;

import com.imd.ufrn.requestProcessors.UdpRequestProcessor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpHandler implements Runnable {

    private DatagramSocket socket;
    private DatagramPacket packet;

    public UdpHandler() {
    }

    public UdpHandler(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {

        String[] tokens = tokenize(packet);

        String operation = tokens[0];
        String path = tokens[1];
        String body = tokens[2];

        UdpRequestProcessor requestProcessor = new UdpRequestProcessor(operation, path, body);
        String response = requestProcessor.process();

        packet = new DatagramPacket(response.getBytes(), response.getBytes().length, packet.getAddress(), packet.getPort());

        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] tokenize(DatagramPacket packet) {
        String[] tokens = new String(packet.getData(), 0, packet.getLength()).split(";");

        String operation = tokens[0];
        String path = tokens[1];

        StringBuilder body = new StringBuilder();

        for (int i = 2; i < tokens.length; i++) {
            body.append(tokens[i]);
            if (i < tokens.length - 1) {
                body.append(";");
            }
        }

        return new String[]{operation, path, body.toString()};

    }

}