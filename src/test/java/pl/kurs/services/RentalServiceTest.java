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
import pl.kurs.dao.IBookDao;
import pl.kurs.dao.IRentalDao;
import pl.kurs.exceptions.BookLockedForRentalException;
import pl.kurs.exceptions.BookNotAvailableException;
import pl.kurs.exceptions.RentalNotFoundException;
import pl.kurs.models.Book;
import pl.kurs.models.Customer;
import pl.kurs.models.Rental;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RentalServiceTest {

    @Mock
    private IBookDao bookDao;

    @Mock
    private IRentalDao rentalDao;

    @InjectMocks
    private RentalService rentalService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        rentalService = new RentalService(rentalDao, bookDao);
    }

    @Test
    public void shouldSaveRental() throws BookLockedForRentalException, BookNotAvailableException {
        // given
        Book book = new Book("title", "author", 2022, "publisher", "category", true, "1", false);
        book.setId(1L);
        book.setAvailable(true);

        Customer customer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");
        customer.setId(1L);

        Rental rental = new Rental(customer, book, LocalDate.of(2023, 4, 23), LocalDate.of(2023, 05, 23));

        Mockito.when(rentalDao.save(rental)).thenReturn(rental);

        // when
        Rental savedRental = rentalService.save(rental);

        // then
        Assertions.assertNotNull(savedRental);
        Assertions.assertTrue(savedRental.getBook().isLockedForRental());
        Mockito.verify(bookDao, Mockito.times(1)).update(book);
        Mockito.verify(rentalDao, Mockito.times(1)).save(rental);
    }

    @Test(expected = BookNotAvailableException.class)
    public void shouldThrowBookNotAvailableException() throws BookLockedForRentalException, BookNotAvailableException {
        // given
        Book book = new Book("title", "author", 2022, "publisher", "category", true, "1", false);
        book.setId(1L);
        book.setAvailable(false);

        Customer customer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");
        customer.setId(1L);

        Rental rental = new Rental(customer, book, LocalDate.of(2023, 4, 23), LocalDate.of(2023, 05, 23));

        rentalService.save(rental);
    }

    @Test(expected = BookLockedForRentalException.class)
    public void shouldThrowBookLockedForRentalException() throws BookLockedForRentalException, BookNotAvailableException {
        // given
        Book book = new Book("title", "author", 2022, "publisher", "category", true, "1", false);
        book.setId(1L);
        book.setLockedForRental(true);

        Customer customer = new Customer("Jan", "Kowalski", "jankowalski@wp.pl");
        customer.setId(1L);

        Rental rental = new Rental(customer, book, LocalDate.of(2023, 4, 23), LocalDate.of(2023, 05, 23));

        rentalService.save(rental);
    }

    @Test(expected = BookLockedForRentalException.class)
    public void shouldThrowBookLockedForRentalException2() throws BookNotAvailableException, BookLockedForRentalException {
        // given
        Book book = new Book("title", "author", 2022, "publisher", "category", true, "1", true);
        book.setId(1L);
        book.setAvailable(true);

        Customer customer = new Customer("John", "Doe", "john.doe@gmail.com");
        customer.setId(1L);

        Rental rental = new Rental(customer, book, LocalDate.of(2023, 4, 23), LocalDate.of(2023, 05, 23));

        rentalService.save(rental);

        Mockito.verify(bookDao, Mockito.never()).update(book);
        Mockito.verify(rentalDao, Mockito.never()).save(rental);
    }

    @Test
    public void shouldReturnBook() throws RentalNotFoundException {
        // given
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setReturned(false);
        Book book = new Book();
        book.setId(1L);
        book.setLockedForRental(true);
        rental.setBook(book);
        Mockito.when(rentalDao.findById(rental.getId())).thenReturn(rental);

        // when
        Rental returnedRental = rentalService.returnBook(rental.getId());

        // then
        Assertions.assertTrue(returnedRental.isReturned());
        Assertions.assertFalse(returnedRental.getBook().isLockedForRental());
        Mockito.verify(bookDao, Mockito.times(1)).update(book);
        Mockito.verify(rentalDao, Mockito.times(1)).update(rental);
    }

    @Test(expected = RentalNotFoundException.class)
    public void shouldThrowRentalNotFoundException() throws RentalNotFoundException {
        // given
        long rentalId = 1L;
        Mockito.when(rentalDao.findById(rentalId)).thenReturn(null);

        // when-then
        rentalService.returnBook(rentalId);
    }

    @Test
    public void shouldGetRentedBooksByCustomerId() {
        // given
        long customerId = 1L;
        List<Book> rentedBooks = new ArrayList<>();
        Book book1 = new Book();
        book1.setId(1L);
        Book book2 = new Book();
        book2.setId(2L);
        rentedBooks.add(book1);
        rentedBooks.add(book2);
        Mockito.when(rentalDao.getRentedBooksByCustomerId(customerId)).thenReturn(rentedBooks);

        // when
        List<Book> result = rentalService.getRentedBooksByCustomerId(customerId);

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(book1));
        Assertions.assertTrue(result.contains(book2));
        Mockito.verify(rentalDao, Mockito.times(1)).getRentedBooksByCustomerId(customerId);
    }


}