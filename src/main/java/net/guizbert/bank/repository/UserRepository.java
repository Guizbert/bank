package net.guizbert.bank.repository;

import net.guizbert.bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    // Check if a user exists by email
    Boolean existsByEmail(String email);

    // Check if a user exists by phone number
    Boolean existsByPhoneNumber(String phoneNumber);
}

