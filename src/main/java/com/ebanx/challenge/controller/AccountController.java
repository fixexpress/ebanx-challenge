package com.ebanx.challenge.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebanx.challenge.model.Account;
import com.ebanx.challenge.service.AccountService;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reset() {
        accountService.reset();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance")
    public ResponseEntity<Integer> getBalance(@RequestParam String account_id) {
        Account account = accountService.getBalance(account_id);
        if (account == null) {
            return ResponseEntity.status(404).body(0);
        }
        return ResponseEntity.ok(account.getBalance());
    }

    @PostMapping("/event")
    public ResponseEntity<Object> handleEvent(@RequestBody Map<String, Object> event) {
        String type = (String) event.get("type");
        if ("deposit".equals(type)) {
            String destination = (String) event.get("destination");
            int amount = (Integer) event.get("amount");
            Account account = accountService.deposit(destination, amount);
            return ResponseEntity.status(201).body(Map.of("destination", account));
        } else if ("withdraw".equals(type)) {
            String origin = (String) event.get("origin");
            int amount = (Integer) event.get("amount");
            Account account = accountService.withdraw(origin, amount);
            if (account == null) {
                return ResponseEntity.status(404).body(0);
            }
            return ResponseEntity.status(201).body(Map.of("origin", account));
        } else if ("transfer".equals(type)) {
            String origin = (String) event.get("origin");
            String destination = (String) event.get("destination");
            int amount = (Integer) event.get("amount");
            if (!accountService.transfer(origin, destination, amount)) {
                return ResponseEntity.status(404).body(0);
            }
            return ResponseEntity.status(201).body(Map.of(
                "origin", accountService.getBalance(origin),
                "destination", accountService.getBalance(destination)
            ));
        }
        return ResponseEntity.badRequest().build();
    }
}
