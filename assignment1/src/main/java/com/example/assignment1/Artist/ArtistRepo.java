package com.example.assignment1.Artist;

import com.example.assignment1.Performance.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepo extends JpaRepository<Artist, Long> {

    public Artist findAllById(Long Id);
}