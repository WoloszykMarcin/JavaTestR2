package pl.kurs.dao;

import org.springframework.stereotype.Repository;
import pl.kurs.models.Book;
import pl.kurs.models.Rental;

import javax.persistence.*;
import java.util.List;

@Repository
public class BookDao implements IBookDao {

    @PersistenceUnit
    private EntityManagerFactory factory;

    @Override
    public Book save(Book book) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        entityManager.persist(book);
        tx.commit();
        entityManager.close();

        return book;
    }

    @Override
    public Book blockForRental(Book book) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        Book bookFromDb = entityManager.find(Book.class, book.getId());
        bookFromDb.setLockedForRental(true);

        tx.commit();
        entityManager.close();

        return bookFromDb;
    }

    @Override
    public List<Book> findAll(int pageNumber, int pageSize) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        List<Book> listOfBooks = entityManager.createQuery("SELECT b FROM Book b", Book.class)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        tx.commit();
        entityManager.close();

        return listOfBooks;
    }

    @Override
    public Book select(long id) {
        EntityManager entityManager = factory.createEntityManager();
        return entityManager.find(Book.class, id);
    }

    @Override
    public Book update(Book book) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        entityManager.merge(book);

        tx.commit();
        entityManager.close();

        return book;
    }

    @Override
    public Book findByRefNum(String refNum) {
        EntityManager entityManager = factory.createEntityManager();
        TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b WHERE b.refNum = :refNum", Book.class);
        query.setParameter("refNum", refNum);
        List<Book> results = query.getResultList();
        entityManager.close();
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }
}
