package com.example.assignment1.Ticket;

import com.example.assignment1.Performance.Performance;
import com.example.assignment1.User.User;
import jakarta.persistence.*;

@Entity
@Table(name = "tickets_tb")
public class Ticket {

    @Id
    @SequenceGenerator(
            name = "ticket_sequence",
            sequenceName = "ticket_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ticket_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private Integer price;
    ;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "performance_id", referencedColumnName = "id")
    private Performance performance;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer ticketsBought;

    public Ticket(Long id, Integer price, Performance performance, User user, Integer ticketsBought) {
        this.id = id;
        this.price = price;
        this.performance = performance;
        this.user = user;
        this.ticketsBought = ticketsBought;
    }
    public Ticket() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getTicketsBought() {
        return ticketsBought;
    }

    public void setTicketsBought(Integer ticketsBought) {
        this.ticketsBought = ticketsBought;
    }
}