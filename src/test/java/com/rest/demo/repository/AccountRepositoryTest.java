package com.rest.demo.repository;

import com.rest.demo.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountRepositoryTest {
    private static final Long ID = 1L;
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository = new AccountRepository();
    }

    @Test
    void save() {
        final Account account = mock(Account.class);
        when(account.getId()).thenReturn(ID);
        accountRepository.save(account);
        Account actual = accountRepository.findById(ID).get();
        assertNotNull(actual);
        assertEquals(account, actual);
    }

    @Test
    void findById() {
        final Account account = mock(Account.class);
        when(account.getId()).thenReturn(ID);
        accountRepository.save(account);

        final Optional<Account> actual = accountRepository.findById(ID);
        assertNotNull(actual);
        assertEquals(account, actual.get());
    }

    @Test
    void findAll() {
        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
        Mockito.when(accountRepository.findAll()).
                thenReturn(java.util.Arrays.asList(new Account(1L, "oneTest", "7656"),
                        new Account(2L, "twoTest", "7656")));
    }
}