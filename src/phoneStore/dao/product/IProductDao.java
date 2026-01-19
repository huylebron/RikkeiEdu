package phoneStore.dao.product;

import phoneStore.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IProductDao {

    Product insertProductDao(Product product);

    void updateProductDao(Product product);

    void deleteProductDao(long id);

    Optional<Product> findByIdProductDao(long id);

    List<Product> findAllProductDao();

    List<Product> findByBrandProductDao(String keyword);

    List<Product> findByPriceRangeProductDao(BigDecimal min, BigDecimal max);

    List<Product> searchByNameWithStock(String keyword);
}
