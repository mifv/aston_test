package com.rest.demo.controller;
;
import com.rest.demo.entity.Account;
import com.rest.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {


    @Autowired
    private AccountService accountService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account.getName(), account.getPin());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable Long accountId, @RequestBody TransactionRequest request) {
        Account account = null;
        account = accountService.deposit(accountId, request.getAmount(), request.getPin());
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable Long accountId, @org.jetbrains.annotations.NotNull @RequestBody TransactionRequest request) {
        Account account = null;
        account = accountService.withdraw(accountId, request.getAmount(), request.getPin());
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{fromAccountId}/transfer/{toAccountId}")
    public ResponseEntity<Map<String, Account>> transfer(
            @PathVariable Long fromAccountId,
            @PathVariable Long toAccountId,
            @RequestBody TransactionRequest request) {
        Map<String, Account> accounts = accountService.transfer(fromAccountId, toAccountId, request.getAmount(), request.getPin());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();
        return ResponseEntity.ok(accounts);

    }
}
