package com.imd.ufrn.clients;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class HttpClient implements Client {

    @Override
    public String sendRequest(String request, InetAddress address, int port) {

        Socket connection = null;
        OutputStream output = null;
        BufferedReader input = null;
        try {
            connection = new Socket(address, port);
            connection.setSoTimeout(10000);
            output = new BufferedOutputStream(connection.getOutputStream());
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        StringBuilder httReq = new StringBuilder();

        String[] tokens = request.split(",");
        String method = tokens[0];
        String path = tokens[1];
        String body = tokens[2];

        httReq.append(method);
        httReq.append(" ");
        httReq.append(path);
        httReq.append(" ");
        httReq.append("HTTP/1.1\r\n");
        httReq.append("Connection: close\r\n");
        httReq.append("Content-Length: ");
        httReq.append(body.length());
        httReq.append("\r\n");
        httReq.append("Content-Type: application/json\r\n");
        httReq.append("Host: ");
        httReq.append(address.getHostAddress());
        httReq.append(":");
        httReq.append(connection.getLocalPort());
        httReq.append("\r\n");
        httReq.append("User-Agent: Mozilla/5.0\r\n");
        httReq.append("\r\n");
        httReq.append(body);
        httReq.append("\r\n\n");

        try {
            output.write(httReq.toString().getBytes());
            output.flush();


            StringBuilder response = new StringBuilder();
            String line;

            while ((line = input.readLine()) != null) {
                response.append(line).append("\n");
            }

            String msg = response.toString();

            return msg;

        } catch (Exception e) {
            e.printStackTrace();
            return "500;ERROR";
        } finally {
            try {
                output.close();
                input.close();
                connection.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}