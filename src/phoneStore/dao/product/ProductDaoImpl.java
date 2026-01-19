package phoneStore.dao.product;

import phoneStore.database.DBConnection;
import phoneStore.entity.Product;
import phoneStore.exception.DatabaseException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDaoImpl implements IProductDao {


    /**
     * @param product
     * @return
     */
    @Override
    public Product insertProductDao(Product product) {
        String sql = "{ call sp_product_insert(?, ?, ?, ?, ?) }";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, product.getName());
            cs.setString(2, product.getBrand());
            cs.setBigDecimal(3, product.getPrice());
            cs.setInt(4, product.getStock());

            cs.registerOutParameter(5, Types.BIGINT);

            cs.execute();

            long id = cs.getLong(5);
            product.setId(id);
            return product;
        } catch (SQLException e) {
            throw new DatabaseException("lỗi khi gọi sp_product_insert", e);
        }
    }

    /**
     * @param product
     */
    @Override
    public void updateProductDao(Product product) {
        String sql = "{ call sp_product_update(?, ?, ?, ?, ?) }";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, product.getId());
            cs.setString(2, product.getName());
            cs.setString(3, product.getBrand());
            cs.setBigDecimal(4, product.getPrice());
            cs.setInt(5, product.getStock());

            cs.execute();

        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi sp_product_update", e);
        }

    }

    /**
     * @param id
     */
    @Override
    public void deleteProductDao(long id) {
        String sql = "{ call sp_product_delete(?) }";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, id);
            cs.execute();

        } catch (SQLException e) {
            // raise excep neu khong dung business ( product co trong invoice_detais )
            throw new DatabaseException("Lỗi khi gọi sp_product_delete", e);
        }


    }

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<Product> findByIdProductDao(long id) {
        String sql = "SELECT * FROM fn_product_find_by_id(?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_product_find_by_id", e);
        }
    }

    /**
     * @return
     */
    @Override
    public List<Product> findAllProductDao() {

        String sql = "SELECT * FROM fn_product_find_all()";
        try (Connection connection = DBConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Product> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(mapRow(resultSet));
            }
            return list;


        } catch (SQLException e) {
            throw new DatabaseException("loi khi goi fn_product_ifnd_all");
        }
    }

    private Product mapRow(ResultSet resultSet) throws SQLException {

        Product product = new Product();
        product.setId(resultSet.getLong("id"));
        product.setName(resultSet.getString("name"));
        product.setBrand(resultSet.getString("brand"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setStock(resultSet.getInt("stock"));

        return product;


    }

    /**
     * @param keyword
     * @return
     */
    @Override
    public List<Product> findByBrandProductDao(String keyword) {
        String sql = "SELECT * FROM fn_product_search_by_brand(?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, keyword == null ? "" : keyword);
            try (ResultSet rs = cs.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_product_search_by_brand", e);
        }
    }

    /**
     * @param min
     * @param max
     * @return
     */
    @Override
    public List<Product> findByPriceRangeProductDao(BigDecimal min, BigDecimal max) {
        String sql = "SELECT * FROM fn_product_search_by_price_range(?, ?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setBigDecimal(1, min);
            cs.setBigDecimal(2, max);

            try (ResultSet rs = cs.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_product_search_by_price_range", e);
        }
    }

    /**
     * @param keyword
     * @return
     */
    @Override
    public List<Product> searchByNameWithStock(String keyword) {
        String sql = "SELECT * FROM fn_product_search_by_name_with_stock(?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, keyword == null ? "" : keyword);
            try (ResultSet rs = cs.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi gọi fn_product_search_by_name_with_stock", e);
        }
    }
}
