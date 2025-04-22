package org.hbn.oattesttool.controller;

import org.hbn.oattesttool.entity.Account;
import org.hbn.oattesttool.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/getAllAccounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        ResponseEntity<List<Account>> response = accountService.getAllAccounts();
        List<Account> accounts = response.getBody();
        return ResponseEntity.status(response.getStatusCode()).body(accounts);
    }


    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        try {
            return accountService.createAccount(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing to createTheAccount.");
        }
    }

    @PutMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            @RequestParam String accountNumber,
            @RequestParam Double amount) {
        return accountService.withdraw(accountNumber, amount);
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount(@RequestParam(required = false) String accountNumber){
        try {
            accountService.deleteAccount(accountNumber);
            return ResponseEntity.ok("Account deleted successfully for account number: " + accountNumber);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the account.");
        }
    }


}
