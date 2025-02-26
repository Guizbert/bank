package net.guizbert.bank.repository;

import net.guizbert.bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html

public interface UserRepository extends JpaRepository<User, Long> {

    /*
        I need to pay more attention to naming my method correctly in this file
        or else, it won't be able to generate my queries

        https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
     */
    // Check if a user exists by email
    Boolean existsByEmail(String email);

    // Check if a user exists based on an account number
    Boolean existsByAccountNumber(String accountNumber);

    // Check if a user exists by phone number
    Boolean existsByPhoneNumber(String phoneNumber);

    // Get the user by his account number
    User findByAccountNumber(String accountNumber);

    List<User> findAll();

    Optional<User> findByEmail(String email);


}

