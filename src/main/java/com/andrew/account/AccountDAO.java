package com.andrew.account;

import com.andrew.DatabaseUtil;
import com.andrew.branch.Branch;
import com.andrew.branch.BranchDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public Account createAccount(long accountNumber, String name, int branchId, double balance) throws Exception {

        String sql = "INSERT INTO accounts(account_number, account_holder_name, branch_id, opening_date, balance) " +
                "VALUES (?, ?, ?, CURRENT_DATE, ?) " +
                "RETURNING account_id, account_number, account_holder_name, branch_id, opening_date, balance";

        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountNumber);
            stmt.setString(2, name);
            stmt.setInt(3, branchId);
            stmt.setDouble(4, balance);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            BranchDAO branchDAO = new BranchDAO();
            Branch branch = branchDAO.getBranchById(rs.getInt("branch_id"));

            return new Account(
                    rs.getInt("account_id"),
                    rs.getLong("account_number"),
                    rs.getString("account_holder_name"),
                    branch,
                    rs.getDate("opening_date").toLocalDate(),
                    rs.getDouble("balance")
            );
        }
    }

    public Account getAccountByNumber(long accountNumber) throws Exception {

        String sql =
                "SELECT a.account_id, a.account_number, a.account_holder_name, a.opening_date, a.balance, " +
                        "b.branch_id, b.branch_name, b.ifsc_code, b.city " +
                        "FROM accounts a " +
                        "JOIN branches b ON a.branch_id = b.branch_id " +
                        "WHERE a.account_number = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Branch branch = new Branch(
                        rs.getInt("branch_id"),
                        rs.getString("branch_name"),
                        rs.getString("ifsc_code"),
                        rs.getString("city")
                );

                return new Account(
                        rs.getInt("account_id"),
                        rs.getLong("account_number"),
                        rs.getString("account_holder_name"),
                        branch,
                        rs.getDate("opening_date").toLocalDate(),
                        rs.getDouble("balance")
                );
            }

            return null;
        }
    }

    public List<Account> getAccountsByUserId(int userId) throws Exception {

        List<Account> list = new ArrayList<>();

        String sql =
                "SELECT a.account_id, a.account_number, a.account_holder_name, a.opening_date, a.balance, " +
                        "b.branch_id, b.branch_name, b.ifsc_code, b.city " +
                        "FROM accounts a " +
                        "JOIN user_accounts ua ON a.account_id = ua.account_id " +
                        "JOIN branches b ON a.branch_id = b.branch_id " +
                        "WHERE ua.user_id = ?";

        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {

                Branch branch = new Branch(
                        rs.getInt("branch_id"),
                        rs.getString("branch_name"),
                        rs.getString("ifsc_code"),
                        rs.getString("city")
                );

                Account account = new Account(
                        rs.getInt("account_id"),
                        rs.getLong("account_number"),
                        rs.getString("account_holder_name"),
                        branch,
                        rs.getDate("opening_date").toLocalDate(),
                        rs.getDouble("balance")
                );

                list.add(account);
            }
        }

        return list;
    }
}