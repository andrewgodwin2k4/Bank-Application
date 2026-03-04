package com.andrew.transaction;

import com.andrew.Transaction;

import java.util.List;

public class TransactionService {

    private TransactionDAO dao = new TransactionDAO();

    public List<Transaction> getTransactions(int accountId) throws Exception {
        return dao.getTransactions(accountId);
    }
}