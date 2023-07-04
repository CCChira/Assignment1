package com.example.assignment1.Middlemen;


import com.example.assignment1.Performance.Performance;
import com.example.assignment1.User.User;

public class PerformanceArtist {

    private User user;
    private Performance performance;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }
}
