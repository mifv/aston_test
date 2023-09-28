package com.rest.demo.service;

import com.rest.demo.entity.Account;
import com.rest.demo.exceptions.InsufficientFundsException;
import com.rest.demo.exceptions.InvalidPinException;
import com.rest.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

  @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(String name, String pin) {
        Account account = new Account();
        account.setName(name);
        account.setPin(pin);
        account.setBalance(BigDecimal.ZERO);
        return accountRepository.save(account);
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Object not found"));
    }

    public Account deposit(Long accountId, BigDecimal amount, String pin) {
        Account account = getAccountById(accountId);
        verifyPin(account, pin);
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }

    private void verifyPin(Account account, String pin) {
        if (!account.getPin().equals(pin)) {
            throw new InvalidPinException("Invalid PIN for account: " + account.getId());
        }

    }

    public Account withdraw(Long accountId, BigDecimal amount, String pin) {
        Account account = getAccountById(accountId);
        verifyPin(account, pin);
        BigDecimal newBalance = account.getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account: " + account.getId());
        }
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }

    public Map<String, Account> transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String pin) {
        Account fromAccount = null;
        fromAccount = getAccountById(fromAccountId);
        verifyPin(fromAccount, pin);
        Account toAccount = null;
        toAccount = getAccountById(toAccountId);
        BigDecimal fromAccountBalance = fromAccount.getBalance();
        if (fromAccountBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account: " + fromAccountId);
        }

        fromAccount.setBalance(fromAccountBalance.subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        Map<String, Account> accounts = new HashMap<>();
        accounts.put("fromAccount", accountRepository.save(fromAccount));
        accounts.put("toAccount", accountRepository.save(toAccount));
        return accounts;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();

    }
}

