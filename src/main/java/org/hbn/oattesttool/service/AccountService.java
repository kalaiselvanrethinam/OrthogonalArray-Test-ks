package org.hbn.oattesttool.service;

import org.hbn.oattesttool.entity.Account;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService{

    ResponseEntity<List<Account>> getAllAccounts();
    ResponseEntity<?> createAccount(Account account);

    ResponseEntity<?> withdraw(String accountNumber, Double amount);

    void deleteAccount(String accountNumber);
}
