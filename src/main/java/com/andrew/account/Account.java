package com.andrew.account;

import java.time.LocalDate;

public class Account {

    private int accountId;
    private long accountNumber;
    private String accountHolderName;
    private String branchName;
    private String ifscCode;
    private LocalDate openingDate;
    private double balance;

    public Account(int accountId, long accountNumber, String accountHolderName,
                   String branchName, String ifscCode, LocalDate openingDate, double balance) {

        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.branchName = branchName;
        this.ifscCode = ifscCode;
        this.openingDate = openingDate;
        this.balance = balance;
    }

    public int getAccountId() { return accountId; }
    public long getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public String getBranchName() { return branchName; }
    public String getIfscCode() { return ifscCode; }
    public LocalDate getOpeningDate() { return openingDate; }
    public double getBalance() { return balance; }
}