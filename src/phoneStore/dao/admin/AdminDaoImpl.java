package phoneStore.dao.admin;

import phoneStore.database.DBConnection;
import phoneStore.entity.Admin;
import phoneStore.exception.DatabaseException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class AdminDaoImpl implements IAdminDao{
    /**
     * @param admin
     * @return
     */
    @Override
    public Admin insertAdminDao(Admin admin) {

        String sql = "{ call sp_admin_insert(?, ?, ?) }";
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

    /**
     * @param id
     * @param newPasswordHash
     */
    @Override
    public void updatePassword(Long id, String newPasswordHash) {

        String sql = "{ call sp_admin_update_password(?, ?) }";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, id);
            cs.setString(2, newPasswordHash);
            cs.execute();

        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi sp_admin_update_password", e);
        }



    }
}
