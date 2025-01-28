package com.imd.ufrn.services;

import com.imd.ufrn.model.entities.Ticket;
import com.imd.ufrn.repositories.TicketPostgresRepository;
import com.imd.ufrn.repositories.TicketRepository;

import java.util.List;

public class TicketService {

    TicketRepository ticketRepository = new TicketPostgresRepository();

    public TicketService() {
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Ticket findById(long id) {
        return ticketRepository.findById(id);
    }

    public Ticket create(Ticket ticket) {
        return ticketRepository.create(ticket);
    }

    public Ticket update(Ticket ticket) {
        return ticketRepository.update(ticket);
    }

    public void delete(long id) {
        ticketRepository.delete(id);
    }

}