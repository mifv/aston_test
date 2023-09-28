package com.rest.demo.repository;

import com.rest.demo.entity.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AccountRepository {

    private Map<Long, Account> accounts = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    public Account save(Account account) {
        if (account.getId() == null) {
            account.setId(idGenerator.getAndIncrement());
        }
        accounts.put(account.getId(), account);
        return account;
    }

    public Optional<Account> findById(Long id) {
        Account account = accounts.get(id);
        return Optional.ofNullable(account);
    }

    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }
}

