package com.example.assignment1.Middlemen;


import com.example.assignment1.Ticket.Ticket;
import com.example.assignment1.User.User;

public class TicketHolder {

    private User user;
    private Ticket ticket;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}