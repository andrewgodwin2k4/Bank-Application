package com.andrew.account;

import com.andrew.branch.Branch;
import com.andrew.branch.BranchDAO;

public class AccountService {

    private AccountDAO dao = new AccountDAO();
    private BranchDAO branchDAO = new BranchDAO();

    public Account createAccount(String name, String ifsc, double deposit) throws Exception {

        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Name must not be empty");

        if(deposit <= 0)
            throw new IllegalArgumentException("Deposit must be positive");

        Branch branch = branchDAO.getBranchByIFSC(ifsc);
        if(branch == null)
            throw new IllegalArgumentException("Invalid IFSC Code");

        while(true) {
            long accountNumber = generateAccountNumber();
            try {
                return dao.createAccount(accountNumber, name, branch.getBranchId(), deposit);
            }
            catch(Exception e) {
                if(e.getMessage().contains("accounts_account_number_key")) {
                    continue;
                }
                throw e;
            }
        }
    }

    public Account getAccount(long accountNumber) throws Exception {
        return dao.getAccountByNumber(accountNumber);
    }

    private long generateAccountNumber() {
        return 1000000000L + (long)(Math.random() * 9000000000L);
    }
}