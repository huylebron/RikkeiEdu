package phoneStore.services.customer;

import phoneStore.entity.Customer;

import java.util.List;

public interface ICustomerService {

    Customer createCustomerService(Customer customer);

    void updateCustomerService(Customer customer);

    void deleteCustomerService(long id);

    Customer getByIdCustomerService(long id);

    List<Customer> getAllCustomerService();
}
