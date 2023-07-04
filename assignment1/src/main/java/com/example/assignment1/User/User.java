package com.example.assignment1.User;
import java.util.*;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.*;
@Entity
@Table(name = "users")
public class User {
        @Id
        @SequenceGenerator(
                name = "user_sequence",
                sequenceName = "user_sequence",
                allocationSize = 1
        )
        @GeneratedValue(
                strategy = GenerationType.SEQUENCE,
                generator = "user_sequence"
        )
        private Long id;

        @Column(nullable = false, unique = true)
        private String userName;

        @Column(nullable = false)
        private String passwordHash;

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        private UserType userType;

        @Column(nullable = false)
        @Value("false")
        private Boolean loggedIn;

        @Transient
        private String newName;
        @Transient
        private String newPassword;

        public User() {
        }

        public User(String userName, String passwordHash, UserType userType) {
            this.userName = userName;
            this.passwordHash = Base64.getEncoder().encodeToString(passwordHash.getBytes());
            this.userType = userType;
            this.loggedIn = Boolean.FALSE;
        }

        public User(String userName, String passwordHash, UserType userType, Boolean loggedIn) {
            this.userName = userName;
            this.passwordHash = Base64.getEncoder().encodeToString(passwordHash.getBytes());
            this.userType = userType;
            this.loggedIn = loggedIn;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPasswordHash() {
            return passwordHash;
        }

        public void setPasswordHash(String passwordHash) {
            this.passwordHash = passwordHash;
        }

        public UserType getUserType() {
            return userType;
        }

        public void setUserType(UserType userType) {
            this.userType = userType;
        }

        public boolean isMatchingPassword(String passwordHash) {
            String candidateHash = Base64.getEncoder().encodeToString(passwordHash.getBytes());
            return candidateHash.equals(passwordHash);
        }

        public Boolean getLoggedIn() {
            return loggedIn;
        }

        public void setLoggedIn(Boolean loggedIn) {
            this.loggedIn = loggedIn;
        }

        public String getNewName() {
            return newName;
        }

        public void setNewName(String newName) {
            this.newName = newName;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

}
