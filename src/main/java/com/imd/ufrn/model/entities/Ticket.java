package com.imd.ufrn.model.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Ticket implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long ticketId;
    private String movieTitle;
    private Integer quantity;
    private Double price;

    public Ticket() {
    }

    public Ticket(Long ticketId, String movieTitle, Integer quantity, Double price) {
        this.ticketId = ticketId;
        this.movieTitle = movieTitle;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(ticketId, ticket.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ticketId);
    }

    @Override
    public String toString() {
        return ticketId +
                ";" + movieTitle +
                ";" + quantity +
                ";" + price;
    }
}
