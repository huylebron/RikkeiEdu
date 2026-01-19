package phoneStore.services.customer;

import phoneStore.dao.customer.ICustomerDao;
import phoneStore.entity.Customer;
import phoneStore.exception.BusinessException;
import phoneStore.exception.DatabaseException;
import phoneStore.exception.NotFoundException;
import phoneStore.exception.ValidationException;
import phoneStore.utils.Validator;

import java.util.List;

public class CustomerServiceImpl implements ICustomerService{

    private final ICustomerDao customerDao;

    public CustomerServiceImpl(ICustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * @param customer
     * @return
     */
    @Override
    public Customer createCustomerService(Customer customer) {
        if (customer == null) throw new ValidationException("Customer không hợp lệ.");
        Validator.validateCustomerFields(customer.getName(), customer.getPhone(), customer.getEmail(), customer.getAddress());
        return customerDao.insertCustomerDao(customer);
    }

    /**
     * @param customer
     */
    @Override
    public void updateCustomerService(Customer customer) {

        if (customer == null || customer.getId() == null) {
            throw new ValidationException("Thiếu ID  customer .");
        }
        Validator.validateCustomerFields(customer.getName(), customer.getPhone(), customer.getEmail(), customer.getAddress());

        getByIdCustomerService(customer.getId()); // check tồn tại
try{
    customerDao.updateCustomerDao(customer);
}catch (DatabaseException e) {
    throw  new DatabaseException("lỗi khi cập nhật customer", e ) ;

}
    }

    /**
     * @param id
     */
    @Override
    public void deleteCustomerService(long id) {

        getByIdCustomerService(id)  ;

        try {
            customerDao.deleteCustomerDao(id);

        }catch (DatabaseException e) {
            throw new BusinessException("không thể xóa customer vì đã phát sinh invoice "  ) ;
        }


    }

    /**
     * @param id
     * @return
     */
    @Override
    public Customer getByIdCustomerService(long id) {
        return customerDao.findByIdCustomerDao(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khách hàng id=" + id));
    }

    /**
     * @return
     */
    @Override
    public List<Customer> getAllCustomerService() {
        return customerDao.findAllCustomerDao();
    }
}
