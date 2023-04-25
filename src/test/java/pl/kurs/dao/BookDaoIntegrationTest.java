package pl.kurs.dao;

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
import pl.kurs.models.Book;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JpaConfig.class)
@ActiveProfiles("dev")
@Transactional
public class BookDaoIntegrationTest {

    @Resource
    private IBookDao bookDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void shouldSaveBookToH2Database() {
        //given
        Book book = new Book("Lśnienie", "Steven King", 2009, "Gal Anonim", "Powieść horror", true, "244", false);

        //when
        Book savedBook = bookDao.save(book);

        //then
        assertNotNull(savedBook.getId());


        Book retrievedBook = jdbcTemplate.queryForObject(
                "SELECT * FROM Book WHERE id = ?",
                new Object[]{savedBook.getId()},
                new BeanPropertyRowMapper<>(Book.class)
        );

        assertNotNull(retrievedBook);
        assertTrue(book.getId().compareTo(retrievedBook.getId()) == 0);
        assertTrue(book.getTitle().compareTo(retrievedBook.getTitle()) == 0);
        assertTrue(book.getAuthor().compareTo(retrievedBook.getAuthor()) == 0);
        assertTrue(book.getRefNum().compareTo(retrievedBook.getRefNum()) == 0);
        assertTrue(book.getCategory().compareTo(retrievedBook.getCategory()) == 0);
        assertTrue(book.getPublisher().compareTo(retrievedBook.getPublisher()) == 0);
        assertTrue(book.getYear().compareTo(retrievedBook.getYear()) == 0);
    }

    @Test
    public void shouldBlockForRental() {
        // given
        Book book = new Book("Lśnienie", "Steven King", 2009, "Gal Anonim", "Powieść horror", true, "244", false);
        bookDao.save(book);

        // when
        Book result = bookDao.blockForRental(book);

        // then
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Book bookFromDb = entityManager.find(Book.class, result.getId());
        entityManager.close();
        assertTrue(bookFromDb.isLockedForRental());
    }

    @Test
    public void shouldFindAll() {
        // given
        Book book1 = new Book("Lśnienie", "Steven King", 2009, "Gal Anonim", "Powieść horror", true, "244", false);
        Book book2 = new Book("Władca pierścieni: Drużyna Pierścienia", "J.R.R. Tolkien", 1954, "Allen & Unwin", "Powieść fantastyczna", true, "123", false);
        bookDao.save(book1);
        bookDao.save(book2);

        // when
        List<Book> result = bookDao.findAll(0, 2);

        // then
        assertEquals(2, result.size());
    }

    @Test
    public void shouldSelect() {
        // given
        Book book = new Book("Lśnienie", "Steven King", 2009, "Gal Anonim", "Powieść horror", true, "244", false);
        bookDao.save(book);

        // when
        Book result = bookDao.select(book.getId());

        // then
        assertTrue(result != null && result.getId().equals(book.getId()));
    }

    @Test
    public void shouldUpdate() {
        // given
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Book book = new Book("Lśnienie", "Steven King", 2009, "Gal Anonim", "Powieść horror", true, "244", false);
        bookDao.save(book);
        book.setTitle("New Title");

        // when
        Book result = bookDao.update(book);

        // then
        assertTrue(result.getTitle().equals("New Title"));

        Book bookFromDb = entityManager.find(Book.class, book.getId());
        assertNotNull(bookFromDb);
        assertTrue(book.getId().compareTo(bookFromDb.getId()) == 0);
        assertTrue(book.getTitle().compareTo(bookFromDb.getTitle()) == 0);
    }

    @Test
    public void shouldFindByRefNum() {
        // given
        Book book = new Book("Lśnienie", "Steven King", 2009, "Gal Anonim", "Powieść horror", true, "244", false);
        bookDao.save(book);

        // when
        Book result = bookDao.findByRefNum(book.getRefNum());

        // then
        assertTrue(book.getRefNum().compareTo(result.getRefNum()) == 0);
        assertTrue(book.getTitle().compareTo(result.getTitle()) == 0);
    }

}