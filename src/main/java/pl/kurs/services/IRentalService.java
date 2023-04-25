package pl.kurs.services;

import pl.kurs.exceptions.BookLockedForRentalException;
import pl.kurs.exceptions.BookNotAvailableException;
import pl.kurs.exceptions.RentalNotFoundException;
import pl.kurs.models.Book;
import pl.kurs.models.Customer;
import pl.kurs.models.Rental;

import java.util.List;

public interface IRentalService {
    Rental save(Rental rental) throws BookLockedForRentalException, BookNotAvailableException;
    Rental returnBook(long id) throws RentalNotFoundException;
    List<Book> getRentedBooksByCustomerId(long customerId);
}
