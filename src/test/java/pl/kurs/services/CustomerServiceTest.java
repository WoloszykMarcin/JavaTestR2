package pl.kurs.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import pl.kurs.dao.ICustomerDao;
import pl.kurs.exceptions.CustomerWithGivenMailAlreadyExistsException;
import pl.kurs.models.Customer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @Mock
    private ICustomerDao customerDao;

    @InjectMocks
    private CustomerService customerService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(customerDao);
    }


    @Test
    public void shouldSaveCustomer() throws CustomerWithGivenMailAlreadyExistsException {
        // given
        Customer customer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");

        Mockito.when(customerDao.findByEmail("jankowalski@wp.pl")).thenReturn(null);
        Mockito.when(customerDao.save(customer)).thenReturn(customer);

        // when
        Customer savedCustomer = customerService.saveCustomer(customer);

        // then
        Assertions.assertNotNull(savedCustomer);
        Mockito.verify(customerDao, Mockito.times(1)).findByEmail("jankowalski@wp.pl");
        Mockito.verify(customerDao, Mockito.times(1)).save(customer);
    }

    @Test(expected = CustomerWithGivenMailAlreadyExistsException.class)
    public void shouldThrowCustomerWithGivenMailAlreadyExistsException() throws CustomerWithGivenMailAlreadyExistsException {
        // given
        Customer customer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");
        Mockito.when(customerDao.findByEmail(customer.getEmail())).thenReturn(customer);

        // when-then
        customerService.saveCustomer(customer);
    }

    @Test
    public void shouldThrowExceptionWhenSavingCustomerWithExistingMail() {
        // given
        Customer customer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");

        Mockito.when(customerDao.findByEmail("jankowalski@wp.pl")).thenReturn(customer);

        // then
        Assertions.assertThrows(CustomerWithGivenMailAlreadyExistsException.class, () -> customerService.saveCustomer(customer));
    }

    @Test
    public void shouldGetAllCustomers() {
        // given
        List<Customer> expectedCustomers = List.of(
                new Customer("Jan", "Kowalski", "jankowalski@wp.pl"),
                new Customer("Jan", "Kowalski", "jankowalski@wp.pl")
        );

        Mockito.when(customerDao.findAll(any(int.class), any(int.class))).thenReturn(expectedCustomers);

        // when
        List<Customer> actualCustomers = customerService.getAllCustomers(1, 10);

        // then
        Assertions.assertEquals(expectedCustomers, actualCustomers);
        Mockito.verify(customerDao, Mockito.times(1)).findAll(1, 10);
    }

    @Test
    public void shouldSelectCustomerById() {
        // given
        Customer expectedCustomer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");
        expectedCustomer.setId(1L);

        Mockito.when(customerDao.select(1L)).thenReturn(expectedCustomer);

        // when
        Customer actualCustomer = customerService.select(1L);

        // then
        Assertions.assertEquals(expectedCustomer, actualCustomer);
        Mockito.verify(customerDao, Mockito.times(1)).select(1L);
    }

}