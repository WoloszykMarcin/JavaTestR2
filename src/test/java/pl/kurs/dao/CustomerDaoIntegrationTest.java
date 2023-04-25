package pl.kurs.dao;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.config.JpaConfig;
import pl.kurs.models.Customer;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JpaConfig.class)
@ActiveProfiles("dev")
@Transactional
public class CustomerDaoIntegrationTest {

    @Resource
    private ICustomerDao customerDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void shouldSaveCustomer() {
        // given
        Customer customer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");

        // when
        Customer savedCustomer = customerDao.save(customer);

        // then
        assertNotNull(savedCustomer.getId());

        Customer retrievedCustomer = jdbcTemplate.queryForObject(
                "SELECT * FROM Customer WHERE id = ?",
                new Object[]{savedCustomer.getId()},
                new BeanPropertyRowMapper<>(Customer.class)
        );

        assertNotNull(retrievedCustomer);

        Assert.assertTrue(customer.getId().compareTo(retrievedCustomer.getId()) == 0);
        Assert.assertTrue(customer.getFirstName().compareTo(retrievedCustomer.getFirstName()) == 0);
        Assert.assertTrue(customer.getLastName().compareTo(retrievedCustomer.getLastName()) == 0);
        Assert.assertTrue(customer.getEmail().compareTo(retrievedCustomer.getEmail()) == 0);
    }

    @Test
    public void shouldSelectCustomerById() {
        // given
        Customer customer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");
        customerDao.save(customer);

        // when
        Customer selectedCustomer = customerDao.select(customer.getId());

        // then
        assertTrue(customer.getId().compareTo(selectedCustomer.getId()) == 0);
        assertTrue(customer.getEmail().compareTo(selectedCustomer.getEmail()) == 0);
    }

    @Test
    public void shouldFindAllCustomers() {
        // given
        Customer customer1 = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");
        Customer customer2 = new Customer("Anna", "Kowalska", "annakowalska@wp.com");
        customerDao.save(customer1);
        customerDao.save(customer2);

        // when
        List<Customer> customers = customerDao.findAll(0, 10);

        // then
        List<Long> ids = customers.stream()
                .map(Customer::getId)
                .collect(Collectors.toList());

        assertTrue(customers.size() > 0);
        assertTrue(ids.contains(customer1.getId()));
        assertTrue(ids.contains(customer2.getId()));
    }

    @Test
    public void shouldFindByEmail() {
        // given
        Customer customer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");
        customerDao.save(customer);

        // when
        Customer foundCustomer = customerDao.findByEmail("jankowalski@wp.pl");

        // then
        assertTrue(customer.getEmail().compareTo(foundCustomer.getEmail()) == 0);
    }

}