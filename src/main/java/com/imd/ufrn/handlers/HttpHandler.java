package com.imd.ufrn.handlers;

import com.imd.ufrn.requestProcessors.RequestProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HttpHandler implements Runnable {

    private static final Logger logger = Logger.getLogger(HttpHandler.class.getName());
    private final Socket socket;

    public HttpHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                OutputStream out = socket.getOutputStream()
        ) {
            // Parsing da requisição HTTP
            HttpRequest request = parseRequest(in);


            HttpResponse response = handleRequest(request);

            // Envio da resposta
            sendResponse(out, response);

        } catch (IOException e) {
            logger.severe("Error handling request: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.warning("Error closing socket: " + e.getMessage());
            }
        }
    }

    // Classe para representar uma requisição HTTP
    private static class HttpRequest {
        String method;
        String path;
        String version;
        Map<String, String> headers = new HashMap<>();
        String body;
    }

    // Classe para representar uma resposta HTTP
    private static class HttpResponse {
        int statusCode;
        String statusMessage;
        Map<String, String> headers = new HashMap<>();
        String body;
    }

    // Parsing da requisição HTTP
    private HttpRequest parseRequest(BufferedReader in) throws IOException {
        HttpRequest request = new HttpRequest();

        // Linha de requisição (ex: GET / HTTP/1.1)
        String requestLine = in.readLine();
        if (requestLine == null) return request;

        String[] parts = requestLine.split(" ", 3);
        if (parts.length >= 3) {
            request.method = parts[0];
            request.path = parts[1];
            request.version = parts[2];
        }

        // Headers
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            int colonIndex = line.indexOf(":");
            if (colonIndex > 0) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                request.headers.put(key, value);
            }
        }

        // Corpo (se houver Content-Length)
        if (request.headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(request.headers.get("Content-Length"));
            char[] body = new char[contentLength];
            in.read(body, 0, contentLength);
            request.body = new String(body);
        }

        return request;
    }

    private HttpResponse handleRequest(HttpRequest request) {
        HttpResponse response = new HttpResponse();
        RequestProcessor requestProcessor = new RequestProcessor(request.method, request.path, request.body);
        try {
            String res = requestProcessor.process();

            if (!res.equalsIgnoreCase("NOT FOUND")) {
                response.statusCode = 200;
                response.statusMessage = "OK";
                response.body = res;
            }else {
                response.statusCode = 404;
                response.statusMessage = "NOT FOUND";
                response.body = "";
            }

        } catch (NoSuchMethodException e) {
            String method = request.method;
            String path = request.path;
            response.statusCode = 405;
            response.statusMessage = "Method Not Allowed";
            response.body = "Method " + method + " not supported for path " + path;
            return response;
        }

        return response;
    }


    private void sendResponse(OutputStream out, HttpResponse response) throws IOException {
        String statusLine = String.format("HTTP/1.1 %d %s\r\n", response.statusCode, response.statusMessage);
        out.write(statusLine.getBytes(StandardCharsets.UTF_8));

        if (!response.headers.containsKey("Content-Type")) {
            response.headers.put("Content-Type", "text/plain");
        }
        response.headers.put("Content-Length", String.valueOf(response.body.length()));

        for (Map.Entry<String, String> header : response.headers.entrySet()) {
            String headerLine = String.format("%s: %s\r\n", header.getKey(), header.getValue());
            out.write(headerLine.getBytes(StandardCharsets.UTF_8));
        }

        out.write("\r\n".getBytes(StandardCharsets.UTF_8));

        if (response.body != null) {
            out.write(response.body.getBytes(StandardCharsets.UTF_8));
        }
    }
}