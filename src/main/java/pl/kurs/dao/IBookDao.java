package pl.kurs.dao;

import pl.kurs.models.Book;
import pl.kurs.models.Rental;

import java.util.List;

public interface IBookDao {
    Book save(Book book);

    Book blockForRental(Book book);
    Book select(long id);
    Book update(Book book);
    List<Book> findAll(int pageNumber, int pageSize);

    Book findByRefNum(String refNum);
}
