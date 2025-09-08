package com.banking.banking_system.service;

import com.banking.banking_system.entity.Account;
import com.banking.banking_system.entity.Transaction;
import com.banking.banking_system.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountService accountService;
    
    @Transactional
    public Transaction deposit(Account account, BigDecimal amount, String description) {
        // Update account balance
        BigDecimal newBalance = account.getBalance().add(amount);
        accountService.updateBalance(account, newBalance);
        
        // Create transaction record
        Transaction transaction = new Transaction(null, account, amount, 
                                                Transaction.TransactionType.DEPOSIT, description);
        return transactionRepository.save(transaction);
    }
    
    @Transactional
    public Transaction withdraw(Account account, BigDecimal amount, String description) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        
        // Update account balance
        BigDecimal newBalance = account.getBalance().subtract(amount);
        accountService.updateBalance(account, newBalance);
        
        // Create transaction record
        Transaction transaction = new Transaction(account, null, amount, 
                                                Transaction.TransactionType.WITHDRAWAL, description);
        return transactionRepository.save(transaction);
    }
    
    @Transactional
    public Transaction transfer(Account fromAccount, Account toAccount, BigDecimal amount, String description) {
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        
        // Update balances
        BigDecimal fromNewBalance = fromAccount.getBalance().subtract(amount);
        BigDecimal toNewBalance = toAccount.getBalance().add(amount);
        
        accountService.updateBalance(fromAccount, fromNewBalance);
        accountService.updateBalance(toAccount, toNewBalance);
        
        // Create transaction record
        Transaction transaction = new Transaction(fromAccount, toAccount, amount, 
                                                Transaction.TransactionType.TRANSFER, description);
        return transactionRepository.save(transaction);
    }
    
    public List<Transaction> getTransactionHistory(Account account) {
        return transactionRepository.findByAccount(account);
    }
    
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}