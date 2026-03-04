package com.andrew.transaction;

import com.andrew.DatabaseUtil;
import com.andrew.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public List<Transaction> getTransactions(int accountId) throws Exception {

        List<Transaction> list = new ArrayList<>();

        String sql = "SELECT * FROM transactions WHERE from_account = ? OR to_account = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("from_account"),
                        rs.getInt("to_account"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("transaction_time")
                );

                list.add(t);
            }
        }

        return list;
    }
}