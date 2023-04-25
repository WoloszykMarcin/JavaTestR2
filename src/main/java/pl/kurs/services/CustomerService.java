package pl.kurs.services;

import org.springframework.stereotype.Service;
import pl.kurs.dao.ICustomerDao;
import pl.kurs.exceptions.CustomerWithGivenMailAlreadyExistsException;
import pl.kurs.models.Book;
import pl.kurs.models.Customer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {

    private final ICustomerDao customerDao;

    public CustomerService(ICustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public Customer saveCustomer(Customer customer) throws CustomerWithGivenMailAlreadyExistsException {
        customer = Optional.ofNullable(customer)
                .filter(x -> Objects.isNull(x.getId()))
                .orElseThrow(RuntimeException::new);

        String email = customer.getEmail();
        Customer existingCustomer = customerDao.findByEmail(email);

        if (existingCustomer != null) {
            throw new CustomerWithGivenMailAlreadyExistsException("Customer with given mail already exists");
        }

        return customerDao.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers(int pageNumber, int pageSize) {
        return customerDao.findAll(pageNumber, pageSize);
    }

    @Override
    public Customer select(long id) {
        return customerDao.select(id);
    }
}
