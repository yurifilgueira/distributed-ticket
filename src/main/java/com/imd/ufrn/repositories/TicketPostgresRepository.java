package com.imd.ufrn.repositories;

import com.imd.ufrn.model.entities.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketPostgresRepository implements TicketRepository{

    private static String url = "jdbc:postgresql://localhost:5432/distributed_tickets_db";
    private static String user = "postgres";
    private static String password = "";

    public Connection getConnection(String url, String user, String password) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);

        return conn;
    }

    @Override
    public List<Ticket> findAll() {

        String findAllQuery = "SELECT id, movie_title, quantity, price FROM tickets";

        try (Connection conn = getConnection(url, user, password)) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(findAllQuery);

            List<Ticket> tickets = new ArrayList<>();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getLong("id"));
                ticket.setMovieTitle(rs.getString("movie_title"));
                ticket.setQuantity(rs.getInt("quantity"));
                ticket.setPrice(rs.getDouble("price"));

                tickets.add(ticket);
            }
            return tickets;
        }catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Ticket findById(Long id) {
        String findByIdQuery = "SELECT id, movie_title, quantity, price FROM tickets WHERE id = ?";

        try (Connection conn = getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(findByIdQuery)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setTicketId(rs.getLong("id"));
                    ticket.setMovieTitle(rs.getString("movie_title"));
                    ticket.setQuantity(rs.getInt("quantity"));
                    ticket.setPrice(rs.getDouble("price"));

                    return ticket;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding ticket by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public Ticket create(Ticket ticket) {
        String insertQuery = "INSERT INTO tickets (movie_title, quantity, price) VALUES (?, ?, ?)";

        try (Connection conn = getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ticket.getMovieTitle());
            stmt.setInt(2, ticket.getQuantity());
            stmt.setDouble(3, ticket.getPrice());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Failed to insert ticket, no rows affected.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ticket.setTicketId(rs.getLong(1));
                } else {
                    throw new SQLException("Failed to retrieve the generated ID.");
                }
            }

            return ticket;

        } catch (SQLException e) {
            throw new RuntimeException("Error creating ticket: " + e.getMessage(), e);
        }
    }


    @Override
    public Ticket update(Ticket ticket) {
        String insertQuery = "UPDATE tickets SET movie_title = ?, quantity = ?, price = ? WHERE id = ?";

        try (Connection conn = getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ticket.getMovieTitle());
            stmt.setInt(2, ticket.getQuantity());
            stmt.setDouble(3, ticket.getPrice());
            stmt.setLong(4, ticket.getTicketId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Failed to insert ticket, no rows affected.");
            }

            return ticket;

        } catch (SQLException e) {
            throw new RuntimeException("Error creating ticket: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(long id) {
        String deleteQuery = "DELETE FROM tickets WHERE id = ?";

        try (Connection conn = getConnection(url, user, password)) {

            PreparedStatement stmt = conn.prepareStatement(deleteQuery);

            stmt.setLong(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to delete ticket, no rows affected.");
            }
        }catch (SQLException e) {
            throw new RuntimeException("Error deleting ticket: " + e.getMessage(), e);
        }
    }
}
