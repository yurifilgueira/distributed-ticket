package com.imd.ufrn.clients;

import java.net.InetAddress;

public interface Client {

    String sendRequest(String request, int port);

}
