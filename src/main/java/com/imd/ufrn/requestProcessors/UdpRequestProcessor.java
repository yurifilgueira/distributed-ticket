package com.imd.ufrn.requestProcessors;

import com.imd.ufrn.model.entities.Ticket;

import javax.xml.crypto.NoSuchMechanismException;

public class UdpRequestProcessor {

    private String operation;
    private String path;
    private String body;

    public UdpRequestProcessor() {
    }

    public UdpRequestProcessor(String operation, String path, String body) {
        this.operation = operation;
        this.path = path;
        this.body = body;
    }

    public String process() {

        switch (operation.toUpperCase()) {
            case "POST":
                return processPostRequest(path, body);
            default:
                throw new NoSuchMechanismException("No such operation: " + operation);
        }

    }

    private String processPostRequest(String path, String body) {

        String[] token = body.split(";");

        Ticket ticket = new Ticket();
        ticket.setTicketId(1L);
        ticket.setMovieTitle(token[0]);
        ticket.setQuantity(Integer.parseInt(token[1]));
        ticket.setPrice(Double.parseDouble(token[2]));

        //TODO(Save ticket)

        return ticket.toString();
    }
}