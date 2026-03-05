package com.andrew.transaction;

import java.time.LocalDateTime;

public class Transaction {

    private int transactionId;
    private long fromAccount;
    private long toAccount;
    private double amount;
    private LocalDateTime transactionTime;
    private String transactionType;

    public Transaction(int transactionId, long fromAccount,
                       long toAccount, double amount,
                       LocalDateTime transactionTime,
                       String transactionType) {

        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.transactionType = transactionType;
    }

    public int getTransactionId() { return transactionId; }
    public long getFromAccount() { return fromAccount; }
    public long getToAccount() { return toAccount; }
    public double getAmount() { return amount; }
    public LocalDateTime getTransactionTime() { return transactionTime; }
    public String getTransactionType() { return transactionType; }
}