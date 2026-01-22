package phoneStore.app;

import phoneStore.constant.MenuConstant;
import phoneStore.entity.Product;
import phoneStore.exception.BusinessException;
import phoneStore.exception.ConsoleErrorHandler;
import phoneStore.exception.NotFoundException;
import phoneStore.services.product.IProductService;
import phoneStore.utils.InputUtil;
import phoneStore.utils.TablePrinter;

import java.math.BigDecimal;
import java.util.List;

public class ProductMenuHandler {

    private final IProductService productService ;

    public ProductMenuHandler(IProductService productService) {
        this.productService = productService;
    }


    public void run() {
        while (true) {
            try {
                System.out.println(MenuConstant.PRODUCT_MENU);
                int choice = InputUtil.readIntInRange("Chon: ", 0, 8);

                switch (choice) {
                    case 1:
                        createProduct();
                        break;
                    case 2:
                        updateProduct();
                        break;
                    case 3:
                        deleteProduct();
                        break;
                    case 4:
                        listAllProduct();
                        break;
                    case 5:
                        findByIdProduct();
                        break;
                    case 6:
                        searchByBrandProduct();
                        break;
                    case 7:
                        searchByPriceRangeProduct();
                        break;
                    case 8:
                        searchByNameWithStockProduct();
                        break;
                    case 0:
                        return;
                    default:
                        break;
                }
            } catch (Exception e) {
                ConsoleErrorHandler.handle(e);
            }
        }
    }

    private void searchByNameWithStockProduct() {

        String keyword = InputUtil.readString("Nhap ten keyword: ");
        List<Product> products = productService.searchByNameWithStockProductService(keyword);
        printProducts("kết quả tìm kiếm theo tên + tồn kho : ", products);




    }

    private void searchByPriceRangeProduct() {
        BigDecimal min = InputUtil.readBigDecimal("Min: ");
        BigDecimal max = InputUtil.readBigDecimal("Max: ");
        List<Product> products = productService.searchByPriceRangeProductService(min, max);
        printProducts("kết quả tìm kiếm theo giá : ", products);



    }

    private void searchByBrandProduct() {
        String keyword = InputUtil.readString(" nhập nhãn hàng :  ");
        List<Product> products = productService.searchByBrandProductService(keyword);
        printProducts("kết quả = ",products);




    }

    private void findByIdProduct() {

        long id = InputUtil.readLong(" nhập id : ");
        Product p = productService.getByIdProductService(id);
        printProducts("kết quả ", List.of(p));


    }

    private void listAllProduct() {

        List<Product> products = productService.getAllProductService();
        printProducts("danh sách sản phẩm", products);
    }

    private void printProducts(String title  , List<Product> products) {

        TablePrinter.printTable(title , new String[]{"id" , "tên" ,"nhãn hiệu" , " giá " , " tồn kho"}, products,
                product -> new String[]{
                        String.valueOf(product.getId()) ,
                        product.getName(),
                        product.getBrand(),
                        TablePrinter.fmtMoney(product.getPrice()),
                        String.valueOf(product.getStock())
                });


    }

    private void deleteProduct() {

        try {
            long id = InputUtil.readLong("Nhập ID điện thoại cần xóa: ");
            // Kiểm tra sản phẩm có tồn tại không
            Product product = productService.getByIdProductService(id);

            // Hiển thị thông tin sản phẩm
            System.out.println("\nTHÔNG TIN ĐIỆN THOẠI CẦN XÓA:");
            printProducts("", List.of(product));

            // Xác nhận xóa
            String confirm = InputUtil.readString("\nBạn có chắc chắn muốn xóa? (y/n): ");
            if (!"y".equalsIgnoreCase(confirm.trim())) {
                System.out.println("Đã hủy thao tác xóa.");
                return;
            }

            // Thực hiện xóa
            productService.deleteProductService(id);
            System.out.println("Đã xóa điện thoại thành công! ID: " + id);


        } catch (BusinessException e) {
            System.out.println("Lỗi nghiệp vụ delele product " + e.getMessage());

        }
    }

    private void updateProduct() {


        long id = InputUtil.readLong("nhập id sản phẩm cần cập nhật  ");
        Product exist = productService.getByIdProductService(id);
        System.out.println(" Sản phẩm hiện tại : " + exist);

        String name = InputUtil.readNotNull("tên mới :  ");
        String brand = InputUtil.readNotNull("nhãn hàng mới :  ");
        BigDecimal price = InputUtil.readBigDecimalPositive("Gía mới :  ");
        int stock = InputUtil.readIntNonNegative("Tồn Kho :   ");

        Product p = new Product(id, name, brand, price, stock) ;
        productService.updateProductService(p);


        System.out.println("cập nhật thành công ! sản phẩm mới :  " + p);


    }


    /**
     *
     */
    private void createProduct() {

        String name = InputUtil.readNotNull("tên sản phẩm : ") ;
        String brand = InputUtil.readNotNull("nhãn hiệu : ") ;
        BigDecimal price = InputUtil.readBigDecimalPositive("Giá : ") ;
        int stock =  InputUtil.readIntNonNegative("tồn kho " ) ;
        Product p = new Product(null, name, brand, price, stock);
        Product created = productService.createProductService(p) ;
        System.out.println(" đã thêm sản phẩm thành công ! id = " + created.getId());




    }
}
