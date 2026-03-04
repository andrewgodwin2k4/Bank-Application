package com.andrew.account;

import com.andrew.DatabaseUtil;

import java.sql.*;

public class AccountDAO {

    public Account createAccount(long accountNumber, String name, String branch, String ifsc, double balance) throws Exception {

        String sql = "INSERT INTO accounts(account_number, account_holder_name, branch_name, ifsc_code, opening_date, balance) " +
                        "VALUES (?, ?, ?, ?, CURRENT_DATE, ?) RETURNING *";

        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountNumber);
            stmt.setString(2, name);
            stmt.setString(3, branch);
            stmt.setString(4, ifsc);
            stmt.setDouble(5, balance);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return new Account(
                    rs.getInt("account_id"),
                    rs.getLong("account_number"),
                    rs.getString("account_holder_name"),
                    rs.getString("branch_name"),
                    rs.getString("ifsc_code"),
                    rs.getDate("opening_date").toLocalDate(),
                    rs.getDouble("balance")
            );
        }
    }

    public Account getAccountById(int id) throws Exception {

        String sql = " SELECT account_id, account_number, account_holder_name, branch_name, ifsc_code, opening_date, balance FROM accounts WHERE account_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getInt("account_id"),
                        rs.getLong("account_number"),
                        rs.getString("account_holder_name"),
                        rs.getString("branch_name"),
                        rs.getString("ifsc_code"),
                        rs.getDate("opening_date").toLocalDate(),
                        rs.getDouble("balance")
                );
            }

            return null;
        }
    }
}