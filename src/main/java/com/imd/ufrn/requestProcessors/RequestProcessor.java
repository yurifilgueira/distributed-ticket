package com.imd.ufrn.requestProcessors;

import com.imd.ufrn.model.entities.Ticket;
import com.imd.ufrn.services.TicketService;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class RequestProcessor {

    private String operation;
    private String path;
    private String body;
    private final TicketService ticketService = new TicketService();
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public RequestProcessor() {
    }

    public RequestProcessor(String operation, String path, String body) {
        this.operation = operation;
        this.path = path;
        this.body = body;
    }

    public String process() throws NoSuchMethodException {

        switch (operation.toUpperCase()) {
            case "POST":
                logger.info("\u001B[34mPOST request receveid\u001B[0m");
                return processPostRequest(body);
            case "PUT":
                logger.info("\u001B[34mPUT request receveid\u001B[0m");
                return processPutRequest(path, body);
            case "GET":
                logger.info("\u001B[34mGET request received\u001B[0m");
                return processGetRequest(path);
            case "DELETE":
                logger.info("\u001B[34mDELETE request received\u001B[0m");
                return processDeleteRequest(path);
            default:
                throw new NoSuchMethodException("No such operation: " + operation);
        }
    }

    private String processDeleteRequest(String path) {

        String[] tokens = Arrays.stream(path.split("/"))
                .filter(token -> !token.isEmpty())
                .toArray(String[]::new);

        long id = Long.parseLong(tokens[1]);

        ticketService.delete(id);

        logger.info("\u001B[34mDELETE request processed\u001B[0m");

        return "DELETED";

    }

    private String processPutRequest(String path, String body) {

        String[] tokens = Arrays.stream(path.split("/"))
                .filter(token -> !token.isEmpty())
                .toArray(String[]::new);

        long id = Long.parseLong(tokens[1]);

        Ticket entity = ticketService.findById(id);

        String[] bodyTokens = body.split(";");
        entity.setMovieTitle(bodyTokens[1]);
        entity.setQuantity(Integer.parseInt(bodyTokens[2]));
        entity.setPrice(Double.parseDouble(bodyTokens[3]));

        entity = ticketService.update(entity);

        logger.info("\u001B[34mPUT request processed\u001B[0m");

        if (entity != null) {
            return "NOT FOUND";
        }

        return entity.toString();

    }

    private String processGetRequest(String path) {
        if (path.equals("/tickets")) {
            List<Ticket> tickets = ticketService.findAll();

            logger.info("\u001B[34mGET(FIND ALL) request processed\u001B[0m");
            return tickets.toString();
        }
        else {
            String[] tokens = Arrays.stream(path.split("/"))
                    .filter(token -> !token.isEmpty())
                    .toArray(String[]::new);

            long id = Long.parseLong(tokens[1]);

            Ticket entity = ticketService.findById(id);

            logger.info("\u001B[34mGET(FIND BY ID) request processed\u001B[0m");
            return entity.toString();
        }
    }

    private String processPostRequest(String body) {

        String[] token = body.split(";");

        Ticket ticket = new Ticket();
        ticket.setTicketId(1L);
        ticket.setMovieTitle(token[0]);
        ticket.setQuantity(Integer.parseInt(token[1]));
        ticket.setPrice(Double.parseDouble(token[2]));

        ticket = ticketService.create(ticket);

        logger.info("\u001B[34mPOST request processed\u001B[0m");

        return ticket.toString();
    }
}