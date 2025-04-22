package org.hbn.oattesttool.entity;

import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    @SequenceGenerator(name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "account_sequence")
    private Long id;
    @Column(nullable = false, unique = true)
    private String accountNumber;
    @Column(nullable = false)
    private String accountType;
    @Column(nullable = false)
    private Double balance;
    @Column(nullable = false)
    private String status;

    public Account(Long id, String accountNumber, String accountType, Double balance, String status) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
    }

    public Account() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
