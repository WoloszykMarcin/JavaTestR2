package pl.kurs.services;

import org.springframework.stereotype.Service;
import pl.kurs.dao.IBookDao;
import pl.kurs.exceptions.BookAlreadyExistsException;
import pl.kurs.models.Book;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookService implements IBookService {

    private final IBookDao bookDao;

    public BookService(IBookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Book saveBook(Book book) throws BookAlreadyExistsException {
        book = Optional.ofNullable(book)
                .filter(x -> Objects.isNull(x.getId()))
                .orElseThrow(RuntimeException::new);

        Book existingBook = bookDao.findByRefNum(book.getRefNum());
        if (existingBook != null) {
            throw new BookAlreadyExistsException("Book with reference number " + book.getRefNum() + " already exists.");
        }

        return bookDao.save(book);
    }

    @Override
    public Book blockForRental(Book book) {
        return bookDao.blockForRental(book);
    }

    @Override
    public List<Book> getAllBooks(int pageNumber, int pageSize) {
        return bookDao.findAll(pageNumber, pageSize);
    }

    @Override
    public Book findById(long id) {
        return bookDao.select(id);
    }
}
