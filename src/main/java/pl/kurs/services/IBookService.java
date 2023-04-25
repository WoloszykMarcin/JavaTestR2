package pl.kurs.services;

import pl.kurs.exceptions.BookAlreadyExistsException;
import pl.kurs.models.Book;

import java.awt.print.Pageable;
import java.util.List;

public interface IBookService {
    Book saveBook(Book book) throws BookAlreadyExistsException;

    Book blockForRental(Book book);
    List<Book> getAllBooks(int pageNumber, int pageSize);

    Book findById(long id);
}
