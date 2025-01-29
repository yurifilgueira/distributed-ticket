package com.imd.ufrn.servers;

public class ServerFactory {

    public static Server getServer(String serverType, int port) {

        if (serverType.equalsIgnoreCase("TCP")) {
            return new TcpServer(port);
        }
        else if (serverType.equalsIgnoreCase("UDP")) {
            return new UdpServer(port);
        } else if (serverType.equalsIgnoreCase("HTTP")) {
            return new HttpServer(port);
        } else {
            throw new RuntimeException("Invalid server type: " + serverType);
        }

    }

}
