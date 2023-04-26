package pl.kurs.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.config.JpaConfig;
import pl.kurs.exceptions.RentalNotFoundException;
import pl.kurs.models.Book;
import pl.kurs.models.Customer;
import pl.kurs.models.Rental;
import pl.kurs.services.IRentalService;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JpaConfig.class)
@ActiveProfiles("dev")
@Transactional
public class RentalDaoIntegrationTest {

    @Resource
    private IRentalDao rentalDao;

    @Resource
    private IBookDao bookDao;

    @Resource
    private ICustomerDao customerDao;
    @Mock
    private IRentalService rentalService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSaveRental() throws RentalNotFoundException {
        // given
        Book book = new Book("title", "author", 2023, "publisher", "category", true, "1", false);
        Book savedBook = bookDao.save(book);

        Customer customer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");
        Customer savedCustomer = customerDao.save(customer);

        Rental rental = new Rental(savedCustomer, savedBook, LocalDate.of(2023, 4, 23), LocalDate.of(2023, 05, 23));

        // when
        Rental savedRental = rentalDao.save(rental);

        // then
        assertNotNull(savedRental.getId());

        Rental retrievedRental = rentalDao.findById(savedRental.getId());

        assertNotNull(retrievedRental);
        assertTrue(rental.getId().compareTo(retrievedRental.getId()) == 0);
        assertTrue(rental.getBook().getId().compareTo(retrievedRental.getBook().getId()) == 0);
        assertTrue(rental.getBook().getRefNum().compareTo(retrievedRental.getBook().getRefNum()) == 0);
        assertTrue(rental.getCustomer().getId().compareTo(retrievedRental.getCustomer().getId()) == 0);
        assertTrue(rental.getRentalDate().compareTo(retrievedRental.getRentalDate()) == 0);
        assertTrue(rental.getReturnDate().compareTo(retrievedRental.getReturnDate()) == 0);
    }

    @Test
    public void shouldFindRentalById() throws RentalNotFoundException {
        // given
        Book book = new Book("title", "author", 2023, "publisher", "category", true, "1", false);
        Book savedBook = bookDao.save(book);

        Customer customer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");
        Customer savedCustomer = customerDao.save(customer);

        Rental rental = new Rental(savedCustomer, savedBook, LocalDate.of(2023, 4, 23), LocalDate.of(2023, 05, 23));
        rentalDao.save(rental);

        // when
        Rental foundRental = rentalDao.findById(rental.getId());

        // then
        assertTrue(rental.getId().compareTo(foundRental.getId()) == 0);
    }

    @Test
    public void shouldUpdateRental() throws RentalNotFoundException {
        // given
        Book book = new Book("title", "author", 2023, "publisher", "category", true, "1", false);
        Book savedBook = bookDao.save(book);

        Customer customer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");
        Customer savedCustomer = customerDao.save(customer);

        Rental rental = new Rental(savedCustomer, savedBook, LocalDate.of(2023, 4, 23), LocalDate.of(2023, 05, 23));
        rentalDao.save(rental);

        rentalService.returnBook(savedBook.getId());

        // when
        Rental updatedRental = rentalDao.update(rental);

        // then
        assertTrue(rental.getId().compareTo(updatedRental.getId()) == 0);
    }

    @Test
    public void shouldGetRentedBooksByCustomerId() {
        // given
        Customer customer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");
        Customer savedCustomer = customerDao.save(customer);

        Book book1 = new Book("title1", "author1", 2023, "publisher1", "category1", true, "1", false);
        Book savedBook = bookDao.save(book1);

        Book book2 = new Book("title2", "author2", 2023, "publisher2", "category2", true, "2", false);
        Book savedBook2 = bookDao.save(book2);

        Rental rental = new Rental(savedCustomer, savedBook, LocalDate.of(2023, 4, 23), LocalDate.of(2023, 05, 23));
        Rental rental2 = new Rental(savedCustomer, savedBook2, LocalDate.of(2023, 4, 23), LocalDate.of(2023, 05, 23));
        rentalDao.save(rental);
        rentalDao.save(rental2);

        // when
        List<Book> rentedBooks = rentalDao.getRentedBooksByCustomerId(customer.getId());

        // then
        List<Long> rentedBookIds = rentedBooks.stream()
                .map(Book::getId)
                .collect(Collectors.toList());

        assertEquals(2, rentedBooks.size());
        assertTrue(rentedBookIds.contains(savedBook.getId()));
        assertTrue(rentedBookIds.contains(savedBook2.getId()));
    }


}