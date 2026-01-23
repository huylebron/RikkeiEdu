package phoneStore.dao.admin;

import phoneStore.database.DBConnection;
import phoneStore.entity.Admin;
import phoneStore.exception.DatabaseException;

import java.sql.*;
import java.util.Optional;

public class AdminDaoImpl implements IAdminDao {

    @Override
    public Admin insertAdminDao(Admin admin) {
        String sql = "call sp_admin_insert(?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, admin.getUsername());
            cs.setString(2, admin.getPassword());
            cs.registerOutParameter(3, Types.BIGINT);

            cs.execute();

            long id = cs.getLong(3);
            admin.setAdminId(id);
            return admin;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi sp_admin_insert", e);
        }
    }

    @Override
    public void updatePassword(Long id, String newPasswordHash) {
        String sql = "call sp_admin_update_password(?, ?)";
        try (Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, id);
            cs.setString(2, newPasswordHash);
            cs.execute();

        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi sp_admin_update_password", e);
        }
    }

    @Override
    public Optional<Admin> findByUsername(String username) {
        String sql = "SELECT * FROM fn_admin_find_by_username(?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Admin a = new Admin();
                    a.setAdminId(rs.getLong("id"));
                    a.setUsername(rs.getString("username"));
                    a.setPassword(rs.getString("password"));
                    return Optional.of(a);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_admin_find_by_username", e);
        }
    }

    @Override
    public Optional<Admin> findById(Long id) {
        String sql = "SELECT * FROM admin WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Admin a = new Admin();
                    a.setAdminId(rs.getLong("id"));
                    a.setUsername(rs.getString("username"));
                    a.setPassword(rs.getString("password"));
                    return Optional.of(a);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi truy vấn admin theo ID", e);
        }
    }
}
