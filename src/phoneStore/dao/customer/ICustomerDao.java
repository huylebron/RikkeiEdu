package phoneStore.dao.customer;

import phoneStore.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerDao {

    Customer insertCustomerDao ( Customer customer);
    void updateCustomerDao(Customer customer) ;
    void deleteCustomerDao(long id ) ;
    List<Customer> findAllCustomerDao() ;
    Optional<Customer> findByIdCustomerDao(long id) ;
}
