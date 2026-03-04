package com.andrew.account;

public class AccountService {

    private AccountDAO dao = new AccountDAO();

    public Account createAccount(String name, String branch, String ifsc, double deposit) throws Exception {

        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Name must not be empty");

        if(deposit < 0)
            throw new IllegalArgumentException("Deposit must be positive");

        long accountNumber = generateAccountNumber();

        return dao.createAccount(accountNumber, name, branch, ifsc, deposit);
    }

    public Account getAccount(int id) throws Exception {
        return dao.getAccountById(id);
    }

    private long generateAccountNumber() {
        return 1000000000L + (long)(Math.random() * 9000000000L);
    }
}