package phoneStore.services.product;

import phoneStore.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {

    Product createProductService(Product product);
    void updateProductService(Product product);

    void deleteProductService(long id);

    Product getByIdProductService(long id);

    List<Product> getAllProductService();

    List<Product> searchByBrandProductService(String keyword);

    List<Product> searchByPriceRangeProductService(BigDecimal min, BigDecimal max);

    List<Product> searchByNameWithStockProductService(String keyword);
}
