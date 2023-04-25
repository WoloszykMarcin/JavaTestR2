package pl.kurs.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import pl.kurs.dao.IBookDao;
import pl.kurs.exceptions.BookAlreadyExistsException;
import pl.kurs.models.Book;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    private IBookDao bookDao;

    @InjectMocks
    private BookService bookService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(bookDao);
    }

    @Test
    public void shouldSaveBook() throws BookAlreadyExistsException {
        // given
        Book book = new Book("title", "author", 2023, "publisher", "category", true, "1", false);

        Mockito.when(bookDao.findByRefNum("1")).thenReturn(null);
        Mockito.when(bookDao.save(book)).thenReturn(book);

        // when
        Book savedBook = bookService.saveBook(book);

        // then
        Mockito.verify(bookDao, Mockito.times(1)).findByRefNum("1");
        Mockito.verify(bookDao, Mockito.times(1)).save(book);
    }

    @Test(expected = BookAlreadyExistsException.class)
    public void shouldThrowBookAlreadyExistsException() throws BookAlreadyExistsException {
        // given
        Book book = new Book("title", "author", 2022, "publisher", "category", true, "1", false);
        Mockito.when(bookDao.findByRefNum(book.getRefNum())).thenReturn(book);

        // when-then
        bookService.saveBook(book);
    }

    @Test
    public void shouldLockForRental() {
        // given
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setLockedForRental(true);

        Mockito.when(bookDao.blockForRental(ArgumentMatchers.any(Book.class))).thenReturn(book);

        // when
        Book lockedBook = bookService.blockForRental(book);

        // then
        Assertions.assertTrue(lockedBook.isLockedForRental());
        Mockito.verify(bookDao, Mockito.times(1)).blockForRental(book);
    }

    @Test
    public void shouldGetAllBooks() {
        // given
        int pageNumber = 0;
        int pageSize = 10;
        List<Book> expectedBooks = Arrays.asList(
                new Book("title1", "author1", 2023, "publisher1", "category1", true, "1", false),
                new Book("title2", "author2", 2023, "publisher2", "category2", true, "2", false),
                new Book("title3", "author3", 2023, "publisher3", "category3", true, "3", false)
        );

        Mockito.when(bookDao.findAll(pageNumber, pageSize)).thenReturn(expectedBooks);

        // when
        List<Book> actualBooks = bookService.getAllBooks(pageNumber, pageSize);

        // then
        Assertions.assertEquals(expectedBooks, actualBooks);
        Mockito.verify(bookDao, Mockito.times(1)).findAll(pageNumber, pageSize);
    }

    @Test
    public void shouldFindBookById() {
        // given
        long id = 1L;
        Book expectedBook = new Book("title", "author", 2023, "publisher", "category", true, "1", false);

        Mockito.when(bookDao.select(id)).thenReturn(expectedBook);

        // when
        Book actualBook = bookService.findById(id);

        // then
        Assertions.assertEquals(expectedBook, actualBook);
        Mockito.verify(bookDao, Mockito.times(1)).select(id);
    }

}