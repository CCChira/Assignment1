package com.example.assignment1.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    public List<User> findUserByUserType(UserType userType);

    public List<User> findUserByUserNameAndPasswordHash(String userName, String passwordHash);

    public User findUserByUserName(String userName);

}