package com.andrew.account;

public class AccountService {

    private AccountDAO dao = new AccountDAO();

    public Account createAccount(String name, double deposit) throws Exception {

        if(name.isEmpty())
            throw new IllegalArgumentException("Name must not be empty");
        if (deposit < 0)
            throw new IllegalArgumentException("Deposit must be positive");

        return dao.createAccount(name, deposit);
    }

    public Account getAccount(int id) throws Exception {
        return dao.getAccountById(id);
    }
}