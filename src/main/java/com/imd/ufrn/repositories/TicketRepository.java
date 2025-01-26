package com.imd.ufrn.repositories;

import com.imd.ufrn.model.entities.Ticket;

import java.sql.SQLException;
import java.util.List;

public interface TicketRepository {

    List<Ticket> findAll();

    Ticket findById(Long id);

    Ticket create(Ticket ticket);

    Ticket update(Ticket ticket);

    void delete(long id);

}