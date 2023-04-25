package pl.kurs.services;

import org.springframework.beans.propertyeditors.CustomMapEditor;
import pl.kurs.exceptions.CustomerWithGivenMailAlreadyExistsException;
import pl.kurs.models.Book;
import pl.kurs.models.Customer;

import java.util.List;

public interface ICustomerService {

    Customer saveCustomer(Customer customer) throws CustomerWithGivenMailAlreadyExistsException;
    List<Customer> getAllCustomers(int pageNumber, int pageSize);
    Customer select(long id);
}
