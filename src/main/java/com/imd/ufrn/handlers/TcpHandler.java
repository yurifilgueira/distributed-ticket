package com.imd.ufrn.handlers;

import com.imd.ufrn.requestProcessors.RequestProcessor;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class TcpHandler implements Runnable {

    private Logger logger = Logger.getLogger(TcpHandler.class.getName());
    private Socket socket;

    public TcpHandler() {
    }

    public TcpHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))
        ) {
            logger.info("\u001B[34mDispatching request\u001B[0m");

            String line = bufferedReader.readLine();

            String[] tokens = tokenize(line);

            String operation = tokens[0];
            String path = tokens[1];
            String body = tokens[2];

            RequestProcessor requestProcessor = new RequestProcessor(operation, path, body);
            String response = requestProcessor.process();

            bufferedWriter.write(response);
            bufferedWriter.flush();
            logger.info("\u001B[32mResponse sent to the client\u001B[0m");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    logger.info("\u001B[32mConnection successfully closed\u001B[0m");
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.warning("\u001B[31mFailed to close the connection\u001B[0m");
            }
        }
    }


    private String[] tokenize(String line) {
        String[] tokens = line.split(";");

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