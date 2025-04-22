package org.hbn.oattesttool.service.impl;

import org.hbn.oattesttool.entity.Account;
import org.hbn.oattesttool.repository.AccountRepository;
import org.hbn.oattesttool.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }
    @Override
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();

        if (accounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
        }

        return ResponseEntity.ok(accounts);
    }


    @Override
    public ResponseEntity<?> createAccount(Account account) {
        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        List<String> missingFields = validateCustomerFields(account);

        if (!missingFields.isEmpty()) {
            return ResponseEntity.badRequest().body(missingFields);
        }

        if (account.getBalance() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // again, could return more descriptive error
        }

        String accountNumber = generateAccountNumber();

        Account accountDetails = new Account(
                account.getId(),
                accountNumber,
                account.getAccountType(),
                account.getBalance(),
                account.getStatus()
        );

        Account savedAccount = accountRepository.save(accountDetails);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(List.of(savedAccount));
    }

    @Override
    public ResponseEntity<?> withdraw(String accountNumber, Double amount) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account not found with AccountNumber: " + accountNumber);
        }

        Account account = optionalAccount.get();

        if (amount == null || amount <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Withdrawal amount must be greater than 0.");
        }

        if (account.getBalance() < amount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient balance.");
        }

        account.setBalance(account.getBalance() - amount);
        Account updatedAccount = accountRepository.save(account);

        return ResponseEntity.ok(updatedAccount);
    }

    @Override
    public void deleteAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()){
            throw new IllegalArgumentException("Account number is required");
        }
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if (account.isEmpty()){
            throw new IllegalArgumentException("No account found for account number: " + accountNumber);
        }
        accountRepository.delete(account.get());

    }

    private List<String> validateCustomerFields(Account account) {
        List<String> missingFields = new ArrayList<>();

        if (account.getAccountType() == null || account.getAccountType().isBlank())
            missingFields.add("Account type is required");

        if (account.getStatus() == null || account.getStatus().isBlank())
            missingFields.add("Account status is required");

        if (account.getBalance() == null)
            missingFields.add("Account balance is required");

        return missingFields;
    }

}
