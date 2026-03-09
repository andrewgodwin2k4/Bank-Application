package com.andrew.transaction;

import com.andrew.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public List<Transaction> getTransactions(long accountNumber) throws Exception {

        List<Transaction> list = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection()) {

            String sql = "SELECT t.transaction_id, " +
                            "a1.account_number AS from_account, " +
                            "a2.account_number AS to_account, " +
                            "t.amount, t.transaction_time " +
                            "FROM transactions t " +
                            "LEFT JOIN accounts a1 ON t.from_account = a1.account_id " +
                            "LEFT JOIN accounts a2 ON t.to_account = a2.account_id " +
                            "WHERE a1.account_number = ? OR a2.account_number = ? " +
                            "ORDER BY t.transaction_time DESC";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, accountNumber);
            stmt.setDouble(2, accountNumber);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getLong("from_account"),
                        rs.getLong("to_account"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("transaction_time").toLocalDateTime()
                );

                list.add(t);
            }
        }

        return list;
    }
}