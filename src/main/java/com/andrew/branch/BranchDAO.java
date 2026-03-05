package com.andrew.branch;

import com.andrew.DatabaseUtil;
import java.sql.*;

public class BranchDAO {

    public Branch getBranchByIFSC(String ifsc) throws Exception {

        String sql = "SELECT * FROM branches WHERE ifsc_code = ?";

        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ifsc);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return new Branch(
                        rs.getInt("branch_id"),
                        rs.getString("branch_name"),
                        rs.getString("ifsc_code"),
                        rs.getString("city")
                );
            }
            return null;
        }
    }

    public Branch getBranchById(int id) throws Exception {

        String sql = "SELECT * FROM branches WHERE branch_id = ?";

        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return new Branch(
                        rs.getInt("branch_id"),
                        rs.getString("branch_name"),
                        rs.getString("ifsc_code"),
                        rs.getString("city")
                );
            }

            return null;
        }
    }
}