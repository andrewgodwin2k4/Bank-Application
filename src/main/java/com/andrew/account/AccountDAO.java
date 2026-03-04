package com.andrew.account;

import com.andrew.DatabaseUtil;

import java.sql.*;

public class AccountDAO {

    public Account createAccount(String name, double balance) throws Exception {

        String sql = "INSERT INTO accounts(name, balance) VALUES (?, ?) RETURNING account_id";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setDouble(2, balance);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            int id = rs.getInt("account_id");

            return new Account(id, name, balance);
        }
    }

    public Account getAccountById(int id) throws Exception {

        String sql = "SELECT account_id, name, balance FROM accounts WHERE account_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getInt("account_id"),
                        rs.getString("name"),
                        rs.getDouble("balance")
                );
            }

            return null;
        }
    }
}