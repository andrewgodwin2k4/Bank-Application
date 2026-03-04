package com.andrew.transfer;

import com.andrew.DatabaseUtil;

import java.sql.*;

public class TransferService {

    public void transfer(int from, int to, double amount) throws Exception {

        Connection conn = DatabaseUtil.getConnection();

        try {
            conn.setAutoCommit(false);

            String balanceSql = "SELECT balance FROM accounts WHERE account_id = ?";
            PreparedStatement balanceStmt = conn.prepareStatement(balanceSql);
            balanceStmt.setInt(1, from);
            ResultSet rs = balanceStmt.executeQuery();

            if (!rs.next()) throw new RuntimeException("Sender account not found");

            double balance = rs.getDouble("balance");

            if (balance < amount)
                throw new RuntimeException("Insufficient balance");

            String debitSql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
            PreparedStatement debitStmt = conn.prepareStatement(debitSql);
            debitStmt.setDouble(1, amount);
            debitStmt.setInt(2, from);
            debitStmt.executeUpdate();

            String creditSql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
            PreparedStatement creditStmt = conn.prepareStatement(creditSql);
            creditStmt.setDouble(1, amount);
            creditStmt.setInt(2, to);
            creditStmt.executeUpdate();

            String txnSql = "INSERT INTO transactions(from_account, to_account, amount) VALUES (?, ?, ?)";
            PreparedStatement txnStmt = conn.prepareStatement(txnSql);
            txnStmt.setInt(1, from);
            txnStmt.setInt(2, to);
            txnStmt.setDouble(3, amount);
            txnStmt.executeUpdate();

            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            throw e;

        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
}