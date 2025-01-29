package com.imd.ufrn;

import com.imd.ufrn.servers.ServerFactory;

public class Main {
    public static void main(String[] args) {

        int port = Integer.parseInt(args[0]);
        String serverType = args[1];

        ServerFactory.getServer(serverType, port).start();
    }
}