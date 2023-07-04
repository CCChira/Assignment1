package com.example.assignment1.User;


import com.example.assignment1.Artist.Artist;
import com.example.assignment1.Performance.Performance;
import com.example.assignment1.Ticket.Ticket;
import com.example.assignment1.Middlemen.PerformanceArtist;
import com.example.assignment1.Middlemen.TicketHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "api/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/all")
    public ResponseEntity<?> getUsers(@RequestBody User user) {
        Pair<?, HttpStatus> pair = userService.findUsers(user);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }

    @GetMapping("/admin/admins")
    public ResponseEntity<?> getAdmins(@RequestBody User user) {
        Pair<?, HttpStatus> pair = userService.findAdmins(user);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }

    @GetMapping("/admin/cashiers")
    public ResponseEntity<?> getCashiers(@RequestBody User user) {
        Pair<?, HttpStatus> pair = userService.findCashiers(user);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }

    @PutMapping("/admin/update")
    public ResponseEntity<?> updateCashier(@RequestBody User user) {
        Pair<String, HttpStatus> pair = userService.updateUser(user);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }

    @PostMapping("/admin/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        Pair<String, HttpStatus> pair = userService.addNewUser(user);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> deleteUser(@RequestBody User user) {
        Pair<String, HttpStatus> pair = userService.deleteUser(user);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
        System.out.println(user.getPasswordHash());
        Pair<String, HttpStatus> pair = userService.login(user);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody User user) {
        Pair<String, HttpStatus> pair = userService.logout(user);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }

    @PostMapping("/admin/performance/add")
    public ResponseEntity<String> addPerformance(@RequestBody Performance performance) {
        Pair<String, HttpStatus> pair = userService.createPerformance(performance);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }
    @PostMapping("/admin/artist/add")
    public ResponseEntity<String> addArtist(@RequestBody Artist artist) {
        Pair<String, HttpStatus> pair = userService.createNewArtist(artist);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }
    @GetMapping("/admin/artist/getAll")
    public ResponseEntity<?> getAllArtists(@RequestBody Artist artist) {
        Pair<?, HttpStatus> pair = userService.getAllArtists(artist);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }
    @DeleteMapping("/admin/performance/delete")
    public ResponseEntity<String> deletePerformance(@RequestBody Performance performance) {
        Pair<String, HttpStatus> pair = userService.deletePerformance(performance);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }

    @GetMapping("/admin/performance/all")
    public ResponseEntity<?> getPerformances() {
        Pair<?, HttpStatus> pair = userService.findPerformances();
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }

    @PutMapping("/admin/performance/update")
    public ResponseEntity<?> getPerformances(@RequestBody Performance performance) {
        Pair<?, HttpStatus> pair = userService.updatePerformance(performance);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }

    @PostMapping("cashier/sell")
    public ResponseEntity<?> sellTicket(@RequestBody TicketHolder
                                                ticketHolder) {
        User user = ticketHolder.getUser();
        Ticket ticket = ticketHolder.getTicket();
        Pair<?, HttpStatus> pair = userService.sellTicket(user, ticket);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }

    @GetMapping("admin/export")
    public ResponseEntity<?> exportTickets(@RequestBody PerformanceArtist
                                                   performanceArtist) throws IOException {
        User user = performanceArtist.getUser();
        Performance performance = performanceArtist.getPerformance();
        Pair<?, HttpStatus> pair = userService.exportTickets(user, performance);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }

    @GetMapping("cashier/list")
    public ResponseEntity<?> listTickets(@RequestBody PerformanceArtist
                                                 performanceArtist) throws IOException {
        User user = performanceArtist.getUser();
        Performance performance = performanceArtist.getPerformance();
        Pair<?, HttpStatus> pair = userService.listTicketsForPerformance(user, performance);
        return new ResponseEntity<>(pair.getFirst(), pair.getSecond());
    }
}