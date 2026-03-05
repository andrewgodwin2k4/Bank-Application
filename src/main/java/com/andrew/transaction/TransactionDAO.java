package com.andrew.transaction;

import com.andrew.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public List<Transaction> getTransactions(long accountNumber) throws Exception {

        List<Transaction> list = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection()) {

            String accSql = "SELECT account_id FROM accounts WHERE account_number = ?";
            PreparedStatement accStmt = conn.prepareStatement(accSql);
            accStmt.setLong(1, accountNumber);

            ResultSet accRs = accStmt.executeQuery();

            if (!accRs.next())
                throw new RuntimeException("Account not found");

            int accountId = accRs.getInt("account_id");

            String sql = "SELECT t.transaction_id, " +
                            "a1.account_number AS from_account, " +
                            "a2.account_number AS to_account, " +
                            "t.amount, t.transaction_time, t.transaction_type" +
                            "FROM transactions t " +
                            "JOIN accounts a1 ON t.from_account = a1.account_id " +
                            "JOIN accounts a2 ON t.to_account = a2.account_id " +
                            "WHERE t.from_account = ? OR t.to_account = ? " +
                            "ORDER BY t.transaction_time DESC";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getLong("from_account"),
                        rs.getLong("to_account"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("transaction_time").toLocalDateTime(),
                        rs.getString("transaction_type")
                );

                list.add(t);
            }
        }

        return list;
    }
}