package com.example.assignment1.Performance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepo extends JpaRepository<Performance, Long> {

    public Performance findPerformanceById(Long id);

}