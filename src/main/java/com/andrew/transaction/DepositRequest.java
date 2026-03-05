package com.andrew.transaction;

public class DepositRequest {

    private long accountNumber;
    private double amount;

    public long getAccountNumber() { return accountNumber; }
    public void setAccountNumber(long accountNumber) { this.accountNumber = accountNumber; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}