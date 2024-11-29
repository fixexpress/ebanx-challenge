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
    public ResponseEntity<Object> reset() {
        accountService.reset();
        return ResponseEntity.status(200).body("OK");
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

        if (type == null || (!"deposit".equals(type) && !"withdraw".equals(type) && !"transfer".equals(type))) {
            return ResponseEntity.badRequest().body("Unsupported or missing event type: " + type);
        }

        try {
            switch (type) {
                case "deposit":
                    return handleDeposit(event);

                case "withdraw":
                    return handleWithdraw(event);

                case "transfer":
                    return handleTransfer(event);

                default:
                    return ResponseEntity.badRequest().body("Unknown event type: " + type);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private ResponseEntity<Object> handleDeposit(Map<String, Object> event) {
        String destination = (String) event.get("destination");
        Integer amount = (Integer) event.get("amount");

        if (destination == null || amount == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid deposit event. Destination and positive amount are required.");
        }

        Account account = accountService.deposit(destination, amount);
        return ResponseEntity.status(201).body(Map.of("destination", account));
    }

    private ResponseEntity<Object> handleWithdraw(Map<String, Object> event) {
        String origin = (String) event.get("origin");
        Integer amount = (Integer) event.get("amount");

        if (origin == null || amount == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid withdraw event. Origin and positive amount are required.");
        }

        Account account = accountService.withdraw(origin, amount);
        if (account == null) {
            return ResponseEntity.status(404).body(0);
        }
        return ResponseEntity.status(201).body(Map.of("origin", account));
    }

    private ResponseEntity<Object> handleTransfer(Map<String, Object> event) {
        String origin = (String) event.get("origin");
        String destination = (String) event.get("destination");
        Integer amount = (Integer) event.get("amount");

        if (origin == null || destination == null || amount == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid transfer event. Origin, destination, and positive amount are required.");
        }

        boolean success = accountService.transfer(origin, destination, amount);
        if (!success) {
            return ResponseEntity.status(404).body(0);
        }

        Account originAccount = accountService.getBalance(origin);
        Account destinationAccount = accountService.getBalance(destination);

        return ResponseEntity.status(201).body(Map.of(
            "origin", originAccount,
            "destination", destinationAccount
        ));
    }
}
