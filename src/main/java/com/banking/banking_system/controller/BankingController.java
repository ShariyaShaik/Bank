package com.banking.banking_system.controller;

import com.banking.banking_system.entity.Account;
import com.banking.banking_system.entity.Transaction;
import com.banking.banking_system.entity.User;
import com.banking.banking_system.service.AccountService;
import com.banking.banking_system.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/banking")
public class BankingController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private TransactionService transactionService;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Account> accounts = accountService.findByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("accounts", accounts);
        return "dashboard";
    }
    
    @PostMapping("/create-account")
    public String createAccount(@RequestParam Account.AccountType accountType,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        try {
            Account account = accountService.createAccount(user, accountType);
            redirectAttributes.addFlashAttribute("success", 
                "Account created successfully! Account Number: " + account.getAccountNumber());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create account: " + e.getMessage());
        }
        
        return "redirect:/banking/dashboard";
    }
    
    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber,
                         @RequestParam BigDecimal amount,
                         @RequestParam String description,
                         RedirectAttributes redirectAttributes) {
        
        try {
            Optional<Account> accountOpt = accountService.findByAccountNumber(accountNumber);
            if (accountOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Account not found");
                return "redirect:/banking/dashboard";
            }
            
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                redirectAttributes.addFlashAttribute("error", "Amount must be greater than zero");
                return "redirect:/banking/dashboard";
            }
            
            transactionService.deposit(accountOpt.get(), amount, description);
            redirectAttributes.addFlashAttribute("success", "Deposit successful!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Deposit failed: " + e.getMessage());
        }
        
        return "redirect:/banking/dashboard";
    }
    
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber,
                          @RequestParam BigDecimal amount,
                          @RequestParam String description,
                          RedirectAttributes redirectAttributes) {
        
        try {
            Optional<Account> accountOpt = accountService.findByAccountNumber(accountNumber);
            if (accountOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Account not found");
                return "redirect:/banking/dashboard";
            }
            
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                redirectAttributes.addFlashAttribute("error", "Amount must be greater than zero");
                return "redirect:/banking/dashboard";
            }
            
            transactionService.withdraw(accountOpt.get(), amount, description);
            redirectAttributes.addFlashAttribute("success", "Withdrawal successful!");
            
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Withdrawal failed: " + e.getMessage());
        }
        
        return "redirect:/banking/dashboard";
    }
    
    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAccountNumber,
                          @RequestParam String toAccountNumber,
                          @RequestParam BigDecimal amount,
                          @RequestParam String description,
                          RedirectAttributes redirectAttributes) {
        
        try {
            Optional<Account> fromAccountOpt = accountService.findByAccountNumber(fromAccountNumber);
            Optional<Account> toAccountOpt = accountService.findByAccountNumber(toAccountNumber);
            
            if (fromAccountOpt.isEmpty() || toAccountOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "One or both accounts not found");
                return "redirect:/banking/dashboard";
            }
            
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                redirectAttributes.addFlashAttribute("error", "Amount must be greater than zero");
                return "redirect:/banking/dashboard";
            }
            
            transactionService.transfer(fromAccountOpt.get(), toAccountOpt.get(), amount, description);
            redirectAttributes.addFlashAttribute("success", "Transfer successful!");
            
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Transfer failed: " + e.getMessage());
        }
        
        return "redirect:/banking/dashboard";
    }
    
    @GetMapping("/transactions/{accountNumber}")
    public String getTransactions(@PathVariable String accountNumber,
                                 HttpSession session,
                                 Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        Optional<Account> accountOpt = accountService.findByAccountNumber(accountNumber);
        if (accountOpt.isEmpty()) {
            return "redirect:/banking/dashboard";
        }
        
        Account account = accountOpt.get();
        List<Transaction> transactions = transactionService.getTransactionHistory(account);
        
        model.addAttribute("user", user); 
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        return "transactions";
    }
}