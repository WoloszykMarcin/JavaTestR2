package pl.kurs.dao;

import pl.kurs.exceptions.RentalNotFoundException;
import pl.kurs.models.Book;
import pl.kurs.models.Rental;

import java.util.List;

public interface IRentalDao {
    Rental save(Rental rental);

    Rental findById(long id) throws RentalNotFoundException;

    Rental update(Rental rental);
    List<Book> getRentedBooksByCustomerId(long customerId);
}
