package pl.kurs.services;

import org.springframework.stereotype.Service;
import pl.kurs.dao.IBookDao;
import pl.kurs.dao.IRentalDao;
import pl.kurs.exceptions.BookLockedForRentalException;
import pl.kurs.exceptions.BookNotAvailableException;
import pl.kurs.exceptions.RentalNotFoundException;
import pl.kurs.models.Book;
import pl.kurs.models.Customer;
import pl.kurs.models.Rental;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RentalService implements IRentalService {

    private final IRentalDao rentalDao;
    private final IBookDao bookDao;

    public RentalService(IRentalDao rentalDao, IBookDao bookDao) {
        this.rentalDao = rentalDao;
        this.bookDao = bookDao;
    }

    @Override
    public Rental save(Rental rental) throws BookLockedForRentalException, BookNotAvailableException {
        rental = Optional.ofNullable(rental)
                .filter(x -> Objects.isNull(x.getId()))
                .orElseThrow(RuntimeException::new);

        if (rental.getBook().isAvailable() == false) {
            throw new BookNotAvailableException("Book is not available");
        }

        if (rental.getBook().isLockedForRental() == true) {
            throw new BookLockedForRentalException("Book locked for rental");
        }

        // check if the book is available for rental
        Book book = rental.getBook();

        // update the book to be locked for rental
        book.setLockedForRental(true);
        bookDao.update(book);

        return rentalDao.save(rental);
    }

    @Override
    public Rental returnBook(long id) throws RentalNotFoundException {
        Rental rentalToUpdate = rentalDao.findById(id);
        if (rentalToUpdate == null) {
            throw new RentalNotFoundException("Cannot find Rental with ID: " + id);
        }

        rentalToUpdate.setReturned(true);
        rentalDao.update(rentalToUpdate);

        // update the book to be available for rental
        Book book = rentalToUpdate.getBook();
        book.setLockedForRental(false);
        bookDao.update(book);

        return rentalToUpdate;
    }

    @Override
    public List<Book> getRentedBooksByCustomerId(long customerId) {
        return rentalDao.getRentedBooksByCustomerId(customerId);
    }
}
