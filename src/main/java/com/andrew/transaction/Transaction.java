package com.andrew;

import java.sql.Timestamp;

public class Transaction {

    private int transactionId;
    private int fromAccount;
    private int toAccount;
    private double amount;
    private Timestamp transactionTime;

    public Transaction(int transactionId, int fromAccount,
                       int toAccount, double amount,
                       Timestamp transactionTime) {

        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.transactionTime = transactionTime;
    }

    public int getTransactionId() { return transactionId; }
    public int getFromAccount() { return fromAccount; }
    public int getToAccount() { return toAccount; }
    public double getAmount() { return amount; }
    public Timestamp getTransactionTime() { return transactionTime; }
}