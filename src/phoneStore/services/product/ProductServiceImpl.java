package phoneStore.services.product;

import phoneStore.dao.product.IProductDao;
import phoneStore.entity.Product;
import phoneStore.exception.BusinessException;
import phoneStore.exception.DatabaseException;
import phoneStore.exception.NotFoundException;
import phoneStore.exception.ValidationException;
import phoneStore.utils.Validator;

import java.math.BigDecimal;
import java.util.List;

public class ProductServiceImpl implements IProductService{
 private final IProductDao productDao ;

    public ProductServiceImpl(IProductDao productDao) {
        this.productDao = productDao;
    }


    /**
     * @param product
     * @return
     */
    @Override
    public Product createProductService(Product product) {
        if (product == null) throw new ValidationException("Product không hợp lệ.");
        Validator.validateProductFields(product.getName(), product.getBrand(), product.getPrice(), product.getStock());
        return productDao.insertProductDao(product);
    }

    /**
     * @param product
     */
    @Override
    public void updateProductService(Product product) {
        if (product == null || product.getId() == null) {
            throw new ValidationException("Thiếu ID sản phẩm.");
        }
        Validator.validateProductFields(product.getName(), product.getBrand(), product.getPrice(), product.getStock());
        // get id

        getByIdProductService(product.getId());

        try {
            productDao.updateProductDao(product);


        }catch (DatabaseException e ) {
            throw new DatabaseException(" lỗi khi cập nhật sản phầm ") ;

        }
    }

    /**
     * @param id
     */
    @Override
    public void deleteProductService(long id) {
        // get id

        getByIdProductService(id) ;
        try {
            productDao.deleteProductDao(id);

        } catch (DatabaseException e) {
            throw new BusinessException(" ko thể xóa sản phẩm do đã phát sinh hóa đơn ") ;
        }


    }

    /**
     * @param id
     * @return
     */
    @Override
    public Product getByIdProductService(long id) {
       return productDao.findByIdProductDao(id)
               .orElseThrow(() -> new NotFoundException(" khong tim thay san pham voi id ="+ id ));
    }

    /**
     * @return
     */
    @Override
    public List<Product> getAllProductService() {
        return productDao.findAllProductDao() ;
    }

    /**
     * @param keyword
     * @return
     */
    @Override
    public List<Product> searchByBrandProductService(String keyword) {
        return productDao.findByBrandProductDao(keyword == null ? "" : keyword) ;

    }

    /**
     * @param min
     * @param max
     * @return
     */
    @Override
    public List<Product> searchByPriceRangeProductService(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) throw new ValidationException("Thiếu min/max.");
        if (min.compareTo(max) > 0) throw new ValidationException("Min không được lớn hơn Max.");
        return productDao.findByPriceRangeProductDao(min, max);
    }

    /**
     * @param keyword
     * @return
     */
    @Override
    public List<Product> searchByNameWithStockProductService(String keyword) {
        return productDao.searchByNameWithStock(keyword == null ? "" : keyword);


    }
}
