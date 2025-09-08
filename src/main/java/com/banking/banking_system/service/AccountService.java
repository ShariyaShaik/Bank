package com.banking.banking_system.service;

import com.banking.banking_system.entity.Account;
import com.banking.banking_system.entity.User;
import com.banking.banking_system.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    public Account createAccount(User user, Account.AccountType accountType) {
        String accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, user, accountType);
        return accountRepository.save(account);
    }
    
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
    
    public List<Account> findByUser(User user) {
        return accountRepository.findByUser(user);
    }
    
    public List<Account> findByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }
    
    public Account updateBalance(Account account, BigDecimal newBalance) {
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }
    
    public BigDecimal getBalance(String accountNumber) {
        Optional<Account> account = findByAccountNumber(accountNumber);
        return account.map(Account::getBalance).orElse(BigDecimal.ZERO);
    }
    
    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = "ACC" + String.format("%010d", new Random().nextInt(1000000000));
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
    
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }
}