package pl.kurs.dao;

import pl.kurs.models.Book;
import pl.kurs.models.Customer;

import java.util.List;

public interface ICustomerDao {
    Customer save(Customer customer);
    Customer select(long id);

    List<Customer> findAll(int pageNumber, int pageSize);
    Customer findByEmail(String email);
}
