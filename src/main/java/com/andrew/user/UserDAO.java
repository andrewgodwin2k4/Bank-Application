package com.andrew.user;

import com.andrew.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public User createUser(String username, String password) throws Exception {

        String sql = "INSERT INTO users(username,password) VALUES (?,?) RETURNING user_id";

        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return new User(rs.getInt("user_id"), username, password);
        }
    }

    public User getUserByUsername(String username) throws Exception {

        String sql = "SELECT * FROM users WHERE username = ?";

        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }

            return null;
        }
    }

    public void linkUserAccount(int userId, int accountId) throws Exception {

        String sql = "INSERT INTO user_accounts(user_id, account_id) VALUES (?, ?)";

        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, accountId);

            stmt.executeUpdate();
        }
    }

}
