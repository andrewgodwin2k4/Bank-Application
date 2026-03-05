package com.andrew.transaction;

import com.andrew.DatabaseUtil;

import java.sql.*;
import java.util.List;

public class TransactionService {

    private TransactionDAO dao = new TransactionDAO();

    public List<Transaction> getTransactions(long accountNumber) throws Exception {
        return dao.getTransactions(accountNumber);
    }

    public void transfer(long fromAccountNumber, long toAccountNumber, double amount) throws Exception {

        Connection conn = DatabaseUtil.getConnection();

        try {
            conn.setAutoCommit(false);

            String senderSql = "SELECT account_id, balance FROM accounts WHERE account_number = ?";
            PreparedStatement senderStmt = conn.prepareStatement(senderSql);
            senderStmt.setLong(1, fromAccountNumber);
            ResultSet senderRs = senderStmt.executeQuery();

            if (!senderRs.next())
                throw new RuntimeException("Sender account not found");

            int senderId = senderRs.getInt("account_id");
            double senderBalance = senderRs.getDouble("balance");

            String receiverSql = "SELECT account_id FROM accounts WHERE account_number = ?";
            PreparedStatement receiverStmt = conn.prepareStatement(receiverSql);
            receiverStmt.setLong(1, toAccountNumber);
            ResultSet receiverRs = receiverStmt.executeQuery();

            if (!receiverRs.next())
                throw new RuntimeException("Receiver account not found");

            int receiverId = receiverRs.getInt("account_id");

            if (senderBalance < amount)
                throw new RuntimeException("Insufficient balance");

            String debitSql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
            PreparedStatement debitStmt = conn.prepareStatement(debitSql);
            debitStmt.setDouble(1, amount);
            debitStmt.setInt(2, senderId);
            debitStmt.executeUpdate();

            String creditSql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
            PreparedStatement creditStmt = conn.prepareStatement(creditSql);
            creditStmt.setDouble(1, amount);
            creditStmt.setInt(2, receiverId);
            creditStmt.executeUpdate();

            String txnSql = "INSERT INTO transactions(from_account, to_account, amount, transaction_type) VALUES (?, ?, ?, 'TRANSFER')";
            PreparedStatement txnStmt = conn.prepareStatement(txnSql);
            txnStmt.setInt(1, senderId);
            txnStmt.setInt(2, receiverId);
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

    public void deposit(long accountNumber, double amount) throws Exception {

        Connection conn = DatabaseUtil.getConnection();

        try {
            conn.setAutoCommit(false);

            String sql = "SELECT account_id FROM accounts WHERE account_number = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, accountNumber);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                throw new RuntimeException("Account not found");

            int accountId = rs.getInt("account_id");

            String updateSql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setDouble(1, amount);
            updateStmt.setInt(2, accountId);
            updateStmt.executeUpdate();

            String txnSql = "INSERT INTO transactions(from_account, to_account, amount, transaction_type) " +
                            "VALUES (NULL, ?, ?, 'DEPOSIT')";

            PreparedStatement txnStmt = conn.prepareStatement(txnSql);
            txnStmt.setInt(1, accountId);
            txnStmt.setDouble(2, amount);
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

    public void withdraw(long accountNumber, double amount) throws Exception {

        Connection conn = DatabaseUtil.getConnection();

        try {

            conn.setAutoCommit(false);

            String sql = "SELECT account_id, balance FROM accounts WHERE account_number = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, accountNumber);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                throw new RuntimeException("Account not found");

            int accountId = rs.getInt("account_id");
            double balance = rs.getDouble("balance");

            if (balance < amount)
                throw new RuntimeException("Insufficient balance");

            String updateSql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setDouble(1, amount);
            updateStmt.setInt(2, accountId);
            updateStmt.executeUpdate();

            String txnSql = "INSERT INTO transactions(from_account, to_account, amount, transaction_type) " +
                            "VALUES (?, NULL, ?, 'WITHDRAW')";

            PreparedStatement txnStmt = conn.prepareStatement(txnSql);
            txnStmt.setInt(1, accountId);
            txnStmt.setDouble(2, amount);
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