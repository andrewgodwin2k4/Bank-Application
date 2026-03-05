package com.andrew.account;

import com.andrew.branch.Branch;
import java.time.LocalDate;

public class Account {

    private int accountId;
    private long accountNumber;
    private String accountHolderName;
    private Branch branch;
    private LocalDate openingDate;
    private double balance;

    public Account(int accountId, long accountNumber, String accountHolderName,
                   Branch branch, LocalDate openingDate, double balance) {

        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.branch = branch;
        this.openingDate = openingDate;
        this.balance = balance;
    }

    public int getAccountId() { return accountId; }
    public long getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public Branch getBranch() { return branch; }
    public LocalDate getOpeningDate() { return openingDate; }
    public double getBalance() { return balance; }
}