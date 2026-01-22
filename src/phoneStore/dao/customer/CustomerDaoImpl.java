package phoneStore.dao.customer;

import phoneStore.database.DBConnection;
import phoneStore.entity.Customer;
import phoneStore.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDaoImpl implements ICustomerDao {

    /**
     * @param customer
     * @return
     */
    @Override
    public Customer insertCustomerDao(Customer customer) {
        String sql = "call sp_customer_insert(?, ?, ?, ?, ?)" ;
        try(Connection connection = DBConnection.getConnection();
            CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getPhone());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getAddress());
            statement.registerOutParameter(5, Types.BIGINT);

            statement.execute();

            long id = statement.getLong(5);
            customer.setId(id);
            return customer;


        } catch (SQLException e) {
            throw new DatabaseException(" lỗi khi gọi sp-customer_insert" , e ) ;
        }



    }

    /**
     * @param customer
     */
    @Override
    public void updateCustomerDao(Customer customer) {
        String sql = "CALL sp_customer_update(?,?,?,?,?)";
        try (Connection connection = DBConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setLong(1, customer.getId());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getEmail());
            statement.setString(5, customer.getAddress());

            statement.execute();

        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi sp_customer_update", e);
        }



    }

    /**
     * @param id
     */
    @Override
    public void deleteCustomerDao(long id) {
        String sql = "call sp_customer_delete(?)";
        try (Connection connection = DBConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setLong(1, id);
            statement.execute();

        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi sp_customer_delete", e);
        }


    }

    /**
     * @return
     */
    @Override
    public List<Customer> findAllCustomerDao() {
        String sql = "SELECT * FROM fn_customer_find_all()";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            List<Customer> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_customer_find_all", e);
        }
    }

    private Customer mapRow(ResultSet rs) throws SQLException {
        Customer customer = new Customer() ;
        customer.setId(rs.getLong("id"));
        customer.setName(rs.getString("name"));
        customer.setPhone(rs.getString("phone"));
        customer.setEmail(rs.getString("email"));
        customer.setAddress(rs.getString("address"));
        return customer ;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<Customer> findByIdCustomerDao(long id) {
       return findAllCustomerDao().stream()
               .filter(customer -> customer.getId() != null && customer.getId() == id )
               .findFirst() ;

    }





}
