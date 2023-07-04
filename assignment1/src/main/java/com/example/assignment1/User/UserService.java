package com.example.assignment1.User;

import com.example.assignment1.Artist.Artist;
import com.example.assignment1.Artist.ArtistRepo;
import com.example.assignment1.Performance.Performance;
import com.example.assignment1.Performance.PerformanceRepo;
import com.example.assignment1.Ticket.Ticket;
import com.example.assignment1.Ticket.TicketRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    private final UserRepo userRepo;
    private final PerformanceRepo performanceRepo;
    private final ArtistRepo artistRepo;
    private final TicketRepo ticketRepo;

    @Autowired
    public UserService(UserRepo userRepo, PerformanceRepo performanceRepo, TicketRepo ticketRepo, ArtistRepo artistRepo) {
        this.userRepo = userRepo;
        this.performanceRepo = performanceRepo;
        this.ticketRepo = ticketRepo;
        this.artistRepo = artistRepo;
    }

    public Pair<?, HttpStatus> findUsers(User user) {
        User foundUsers = findUserByCredentials(user.getUserName(), user.getPasswordHash());
        if (foundUsers.getLoggedIn().equals(Boolean.FALSE)) {
            return Pair.of("User is not logged in", HttpStatus.BAD_REQUEST);
        }
        if (!foundUsers.getUserType().equals(UserType.ADMIN)) {
            return Pair.of("User does not have permissions", HttpStatus.BAD_REQUEST);
        }

        return Pair.of(userRepo.findAll(), HttpStatus.OK);
    }

    public Pair<?, HttpStatus> findAdmins(User user) {
        User foundUsers = findUserByCredentials(user.getUserName(), user.getPasswordHash());
        if (foundUsers.getLoggedIn().equals(Boolean.FALSE)) {
            return Pair.of("User is not logged in", HttpStatus.BAD_REQUEST);
        }
        if (!foundUsers.getUserType().equals(UserType.ADMIN)) {
            return Pair.of("User does not have permissions", HttpStatus.BAD_REQUEST);
        }

        return Pair.of(userRepo.findUserByUserType(UserType.ADMIN), HttpStatus.OK);
    }

    public Pair<?, HttpStatus> findCashiers(User user) {
        User foundUsers = findUserByCredentials(user.getUserName(), user.getPasswordHash());
        if (foundUsers.getLoggedIn().equals(Boolean.FALSE)) {
            return Pair.of("User is not logged in", HttpStatus.BAD_REQUEST);
        }
        if (!foundUsers.getUserType().equals(UserType.ADMIN)) {
            return Pair.of("User does not have permissions", HttpStatus.BAD_REQUEST);
        }

        return Pair.of(userRepo.findUserByUserType(UserType.CASHIER), HttpStatus.OK);
    }

    public Pair<String, HttpStatus> addNewUser(User user) {
        if (userRepo.findUserByUserName(user.getUserName()) != null) {
            return Pair.of("Username already exists.", HttpStatus.BAD_REQUEST);
        }
        user.setPasswordHash(Base64.getEncoder().encodeToString(user.getPasswordHash().getBytes()));
        user.setLoggedIn(Boolean.FALSE);
        userRepo.save(user);
        return Pair.of("User has been added", HttpStatus.OK);
    }

    public User findUserByCredentials(String userName, String password) {
        Optional<User> foundUser = userRepo.findUserByUserNameAndPasswordHash(
                        userName, Base64.getEncoder().encodeToString(password.getBytes()))
                .stream().findFirst();
        return foundUser.orElse(null);
    }

    public Pair<String, HttpStatus> login(User user) {
        String userName = user.getUserName();
        String password = user.getPasswordHash();

        User foundUser = findUserByCredentials(userName, password);
        if (foundUser == null) {
            return Pair.of("Username or password incorrect.", HttpStatus.BAD_REQUEST);
        }

        if (foundUser.getLoggedIn().equals(Boolean.TRUE)) {
            return Pair.of("User already logged in.", HttpStatus.BAD_REQUEST);
        }

        foundUser.setLoggedIn(Boolean.TRUE);
        userRepo.save(foundUser);
        return Pair.of("You have successfully logged in", HttpStatus.OK);
    }

    public Pair<String, HttpStatus> logout(User user) {
        String userName = user.getUserName();
        String password = user.getPasswordHash();

        User foundUser = userRepo.findUserByUserName(userName);
        if (foundUser == null) {
            return Pair.of("Username incorrect.", HttpStatus.BAD_REQUEST);
        }

        if (foundUser.getLoggedIn().equals(Boolean.FALSE)) {
            return Pair.of("User is not logged in.", HttpStatus.BAD_REQUEST);
        }

        foundUser.setLoggedIn(Boolean.FALSE);
        userRepo.save(foundUser);
        return Pair.of("You have successfully logged out", HttpStatus.OK);
    }

    public Pair<String, HttpStatus> updateUser(User user) {
        String userName = user.getUserName();

        User foundUser = userRepo.findUserByUserName(userName);
        if (foundUser == null) {
            return Pair.of("Username incorrect.", HttpStatus.BAD_REQUEST);
        }

        if (foundUser.getUserType().equals(UserType.ADMIN)) {
            return Pair.of("Cannot update admin.", HttpStatus.BAD_REQUEST);
        }

        foundUser.setUserName(user.getNewName());
        foundUser.setPasswordHash(Base64.getEncoder().encodeToString(user.getNewPassword().getBytes()));
        userRepo.save(foundUser);
        return Pair.of("User has been updated", HttpStatus.OK);
    }

    public Pair<String, HttpStatus> deleteUser(User user) {
        String userName = user.getUserName();

        User foundUser = userRepo.findUserByUserName(userName);
        if (foundUser == null) {
            return Pair.of("User cannot be found.", HttpStatus.BAD_REQUEST);
        }

        userRepo.delete(foundUser);
        return Pair.of("User has been deleted", HttpStatus.OK);
    }

    public Pair<String, HttpStatus> createPerformance(Performance performance) {
        if (performance.getTickets() > 20000) {
            return Pair.of("Maximum 20000 tickets allowed", HttpStatus.BAD_REQUEST);
        }

        performanceRepo.save(performance);
        return Pair.of("Performance has been created", HttpStatus.OK);
    }

    public Pair<String, HttpStatus> deletePerformance(Performance performance) {
        Performance foundPerformance = performanceRepo.findPerformanceById(performance.getId());
        if (foundPerformance == null) {
            return Pair.of("Performance cannot be found.", HttpStatus.BAD_REQUEST);
        }

        performanceRepo.delete(performance);
        return Pair.of("Performance has been deleted", HttpStatus.OK);
    }

    public Pair<?, HttpStatus> findPerformances() {
        return Pair.of(performanceRepo.findAll(), HttpStatus.OK);
    }

    public Pair<String, HttpStatus> updatePerformance(Performance performance) {
        Performance foundPerformance = performanceRepo.findPerformanceById(performance.getId());
        if (foundPerformance == null) {
            return Pair.of("Performance cannot be found", HttpStatus.OK);
        }

        foundPerformance = performance;
        performanceRepo.save(foundPerformance);
        return Pair.of("Performance has been updated", HttpStatus.OK);
    }

    public Pair<String, HttpStatus> sellTicket(User user, Ticket ticket) {
        String userName = user.getUserName();
        String password = user.getPasswordHash();

        User foundUser = findUserByCredentials(userName, password);
        if (foundUser == null) {
            return Pair.of("Username or password incorrect.", HttpStatus.BAD_REQUEST);
        }

        if (foundUser.getUserType().equals(UserType.ADMIN)) {
            return Pair.of("Admin cannot do this operation.", HttpStatus.BAD_REQUEST);
        }

        if (foundUser.getLoggedIn().equals(Boolean.FALSE)) {
            return Pair.of("User is not logged in.", HttpStatus.BAD_REQUEST);
        }

        Performance foundPerformance = performanceRepo.findPerformanceById(ticket.getPerformance().getId());
        List<Ticket> boughtTickets = ticketRepo.findByPerformance(foundPerformance);

        Integer alreadySoldTickets = 0;
        if (!boughtTickets.isEmpty()) {
            alreadySoldTickets = boughtTickets.stream()
                    .map(Ticket::getTicketsBought)
                    .reduce(Integer::sum).get();
        }

        if (alreadySoldTickets + ticket.getTicketsBought() > foundPerformance.getTickets()) {
            return Pair.of("Cannot buy that many tickets for this show", HttpStatus.BAD_REQUEST);
        }

        ticket.setUser(foundUser);
        ticket.setPerformance(performanceRepo.findPerformanceById(ticket.getPerformance().getId()));

        ticketRepo.save(ticket);
        return Pair.of("Ticket has been added.", HttpStatus.OK);
    }

    public Pair<String, HttpStatus> exportTickets(User user, Performance performance) throws IOException {
        System.out.println(user.getUserName() + " " + user.getPasswordHash());
        User foundUsers = findUserByCredentials(user.getUserName(), user.getPasswordHash());
        if (foundUsers.getLoggedIn().equals(Boolean.FALSE)) {
            return Pair.of("User is not logged in", HttpStatus.BAD_REQUEST);
        }
        if (!foundUsers.getUserType().equals(UserType.ADMIN)) {
            return Pair.of("User does not have permissions", HttpStatus.BAD_REQUEST);
        }

        Performance foundPerformance = performanceRepo.findPerformanceById(performance.getId());
        if (foundPerformance == null) {
            return Pair.of("Cannot find performance", HttpStatus.BAD_REQUEST);
        }

        List<Ticket> foundTickets = ticketRepo.findByPerformance(foundPerformance);
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        mapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get("tickets.json").toFile(), foundTickets);

        return Pair.of("Tickets exported", HttpStatus.OK);
    }
    public Pair<String, HttpStatus> createNewArtist(Artist artist) {
        artistRepo.save(artist);
        return Pair.of("Artist has been created", HttpStatus.OK);
    }
    public Pair<?, HttpStatus> getAllArtists(Artist artist) {
        List<Artist> artists = artistRepo.findAll();
        return Pair.of(artists, HttpStatus.OK);
    }
    public Pair<?, HttpStatus> listTicketsForPerformance(User user, Performance performance) {
        User foundUsers = findUserByCredentials(user.getUserName(), user.getPasswordHash());
        if (foundUsers.getLoggedIn().equals(Boolean.FALSE)) {
            return Pair.of("User is not logged in", HttpStatus.BAD_REQUEST);
        }
        if (!foundUsers.getUserType().equals(UserType.CASHIER)) {
            return Pair.of("User does not have permissions", HttpStatus.BAD_REQUEST);
        }

        Performance foundPerformance = performanceRepo.findPerformanceById(performance.getId());
        if (foundPerformance == null) {
            return Pair.of("Cannot find performance", HttpStatus.BAD_REQUEST);
        }

        List<Ticket> tickets = ticketRepo.findByPerformance(performance);
        return Pair.of(tickets, HttpStatus.OK);

    }

}