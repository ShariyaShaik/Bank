package com.banking.banking_system.repository;

import com.banking.banking_system.entity.Account;
import com.banking.banking_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByUser(User user);
    List<Account> findByUserId(Long userId);
    boolean existsByAccountNumber(String accountNumber);
}