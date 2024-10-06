package com.ebanx.challenge.service;

import com.ebanx.challenge.model.Account;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {

    private Map<String, Account> accounts = new HashMap<>();

    public void reset() {
        accounts.clear();
    }

    public Account getBalance(String accountId) {
        return accounts.get(accountId);
    }

    public Account deposit(String destination, int amount) {
        Account account = accounts.getOrDefault(destination, new Account(destination, 0));
        account.setBalance(account.getBalance() + amount);
        accounts.put(destination, account);
        return account;
    }

    public Account withdraw(String origin, int amount) {
        Account account = accounts.get(origin);
        if (account == null || account.getBalance() < amount) {
            return null;
        }
        account.setBalance(account.getBalance() - amount);
        return account;
    }

    public boolean transfer(String origin, String destination, int amount) {
        Account originAccount = withdraw(origin, amount);
        if (originAccount == null) {
            return false;
        }
        deposit(destination, amount);
        return true;
    }
}
